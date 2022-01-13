package hydraulics;

import data.DiscreteData;

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
	private WaterLevelCalculator<?, ?> calculator;
	private ManningsEquation equation;

	public ManningsModel(DiscreteData<?, ?> data)
	{
		this.calculator = new WaterLevelCalculator<>(data, 0);
		this.equation = new ManningsEquation();
	}

	public void setN(Number n)
	{
		this.equation.n = n.doubleValue();
	}

	public void setS(Number s)
	{
		this.equation.s = s.doubleValue();
	}

	public boolean calcWaterLevelValid()
	{
		return this.equation.n != null && this.equation.n != 0 && this.equation.s != null;
	}

	// If there does not exist a water level that satisfies the equation, will return a water level that is out of bounds of data
	// Requires n and s to be set
	public double calcWaterLevel(double discharge, double step)
	{
		this.equation.q = discharge;
		this.calculator.moveToLowest();
	
		// Increase water level until either the equation is satisfied or the water overflows the banks
		// The loop stops if the calculated value exceeds the desired value
		double waterLevel;
		for (waterLevel = this.calculator.getWaterLevel().doubleValue(); 
			this.calculator.withinBounds() && this.calcConsistent(waterLevel, -1) == -1; 
			waterLevel += step)
		{ }

		// If the calculator calculates a value, 
		if (this.calculator.withinBounds())
		{
			double q1 = this.calcDischarge(waterLevel);
			double q2 = this.calcDischarge(waterLevel - step);
			this.resetModel();
			return closest(discharge, q1, q2) == q1 ? waterLevel : waterLevel - step;
		}
		else
		{
			this.resetModel();
			return this.calculator.getWaterLevel().doubleValue();
		}

	}

	public double calcVelocity(double discharge, double level)
	{
		this.calculator.setWaterLevel(level);
		this.equation.q = discharge;
		this.equation.a = this.calculator.crossSectionArea();

		double velocity = this.equation.calcVelocity();

		this.equation.q = null;
		this.equation.a = null;
		return velocity;
	}

	public double calcArea(double level)
	{
		this.calculator.setWaterLevel(level);
		return this.calculator.crossSectionArea();
	}

	public double calcDischarge(double level)
	{
		this.setupModel(level);
		double discharge = this.equation.calcDischarge();
		this.resetModel();
		return discharge;
	}

	public double calcPerimeter(double level)
	{
		this.calculator.setWaterLevel(level);
		return this.calculator.wettedPerimeter();
	}

	public boolean withinBounds(Number level)
	{
		this.calculator.setWaterLevel(level);
		return this.calculator.withinBounds();
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
	private double closest(double n, double a, double b)
	{
		double aDist = Math.abs(n - a);
		double bDist = Math.abs(n - b);
		return aDist <= bDist ? a : b;
	}
}
