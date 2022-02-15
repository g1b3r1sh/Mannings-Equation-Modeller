package hydraulics;

import java.util.function.BooleanSupplier;

import data.functions.DiscreteData;

/* Equation Analysis
Givens: n, S
Input: Q
Unknowns: A, P, V

A and P are both calculated using WaterLevelCalculator
	Calculated by changing the water level until manningFormulaConsistent() is true
V is calculated when A is known (i.e. when manningFormulaConsistent() is true)
	Calculated using calcVelocity()
*/

/**
 * Represents a model of the Manning's Equation simulated with a cross-section represented with DiscreteData
**/

public class ManningsModel
{
	private CrossSectionCalculator<?, ?> calculator;
	private ManningsEquation equation = new ManningsEquation();

	public ManningsModel(DiscreteData<?, ?> data)
	{
		this.calculator = new CrossSectionCalculator<>(data, 0);
	}

	public ManningsModel(ManningsModel model)
	{
		this(model.getSectionData());
		this.setN(model.equation.n);
		this.setS(model.equation.s);
	}

	public DiscreteData<?, ?> getSectionData()
	{
		return this.calculator.getSectionData();
	}

	public void setN(Number n)
	{
		this.equation.n = n.doubleValue();
	}

	public void setS(Number s)
	{
		this.equation.s = s.doubleValue();
	}

	public boolean areConstantsSet()
	{
		return this.equation.n != null && this.equation.n != 0 && this.equation.s != null && this.equation.s != 0;
	}

	public boolean dischargeUnderflow(double discharge)
	{
		return discharge < 0;
	}

	public boolean canUseData()
	{
		return this.getSectionData().size() >= 2;
	}

	// If interrupted by runCondition returning false before finishing loop, returns null
	private Double calcWaterLevel(double discharge, double initStep, double hint, BooleanSupplier runCondition)
	{
		if (!this.areConstantsSet())
		{
			throw new IllegalStateException("Model is not ready.");
		}
		if (this.dischargeUnderflow(discharge))
		{
			throw new IllegalArgumentException("Discharge must be above 0.");
		}
		if (initStep == 0)
		{
			throw new IllegalArgumentException("Step cannot be 0.");
		}
		if (!this.canUseData())
		{
			throw new IllegalArgumentException("Data cannot be used to calculate water level.");
		}

		this.equation.q = discharge;
		double lowestWaterLevel = this.calculator.getLowest().doubleValue();
		hint = Math.max(hint, lowestWaterLevel);
		int offsetDir = this.calcConsistent(hint, -1);
		// Increment is positive or negative, depending on if desired discharge is greater or less than discharge calculated using hint
		double increment = offsetDir == -1 ? Math.abs(initStep) : -1 * Math.abs(initStep);

		// Special case to avoid infinite loop, since rest of algorithm assumes hint is not the correct water level
		if (this.calcConsistent(hint, 0) == 0)
		{
			return hint;
		}

		// Starting with water level set to hint, increment it until calculated exceeds the desired discharge, 
		// 		meaning that the current water level has passed the correct water level
		double waterLevel;
		for (waterLevel = hint;
			this.calcConsistent(waterLevel, -1) == offsetDir; 
			waterLevel += increment)
		{
			if (!runCondition.getAsBoolean())
			{
				this.resetModel();
				return null;
			}
		}

		// Two levels that are on either side of the correct level
		// To ensure consistency in return value when given differing hints, level1 < level2
		double level1;
		double level2;
		if (increment > 0)
		{
			level1 = waterLevel;
			level2 = waterLevel - increment;
		}
		else
		{
			level1 = waterLevel - increment;
			level2 = waterLevel;
		}

		// Return the level that gives the discharge closest to the desired discharge
		double q1 = this.calcDischarge(level1);
		double q2 = this.calcDischarge(level2);
		this.resetModel();
		// Note: If both discharges are same distance away from desired discharge, level1 will be returned
		return ManningsModel.closest(discharge, q1, q2) == q1 ? level1 : level2;
	}

	public Double calcWaterLevel(double discharge, int displayScale, BooleanSupplier runCondition)
	{
		if (!this.canUseData())
		{
			throw new IllegalArgumentException("Data cannot be used to calculate water level.");
		}

		Double waterLevel = this.calculator.getLowest().doubleValue();
		// Jump Search: Start by incrementing by a large value, then use the resulting level as a hint for smaller increments
		for (int i = 0 < displayScale ? 0 : displayScale; i <= displayScale; i++)
		{
			waterLevel = this.calcWaterLevel(discharge, ManningsModel.defaultStep(i), waterLevel, runCondition);
			if (waterLevel == null)
			{
				return null;
			}
		}
		return waterLevel;
	}

	public Double calcWaterLevel(double discharge, int displayScale)
	{
		return this.calcWaterLevel(discharge, displayScale, () -> true);
	}

	public double calcDischarge(double level)
	{
		if (!this.areConstantsSet())
		{
			throw new IllegalStateException("Model is not ready.");
		}
		this.setupModel(level);
		double discharge = this.equation.calcDischarge();
		this.resetModel();
		return discharge;
	}

	public double calcVelocity(double discharge, double level)
	{
		this.setupModel(level);
		this.equation.q = discharge;

		double velocity = this.equation.calcVelocity();

		this.resetModel();
		return velocity;
	}

	private void setupModel(double waterLevel)
	{
		this.calculator.setWaterLevel(waterLevel);
		// Warning: If water level overflows, a and p will both be zero, invalidating the equation
		this.equation.a = this.calculator.crossSectionArea();
		this.equation.p = this.calculator.wettedPerimeter();	
	}

	// Reset model so that if any values are accidentally not set, the function throws a null pointer exception
	private void resetModel()
	{
		this.equation.q = null;
		this.equation.a = null;
		this.equation.p = null;
	}

	private int calcConsistent(double waterLevel, double threshold)
	{
		this.setupModel(waterLevel);
		return this.equation.manningFormulaConsistent(threshold);
	}

	// Returns the number that is closest to n
	private static double closest(double n, double a, double b)
	{
		double aDist = Math.abs(n - a);
		double bDist = Math.abs(n - b);
		return aDist <= bDist ? a : b;
	}

	// By, default, steps by 10^-(scale of displayed values + 1)
	private static double defaultStep(int displayScale)
	{
		return Math.pow(0.1, displayScale + 1);
	}
}
