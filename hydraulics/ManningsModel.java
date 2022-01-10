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

	// If there does not exist a water level that satisfies the equation, will return a water level that is out of bounds of data
	// Requires n and s to be set
	public double calcWaterLevel(double discharge, double step)
	{
		this.equation.q = discharge;
		this.calculator.moveToLowest();
		this.equation.a = this.calculator.crossSectionArea();
		this.equation.p = this.calculator.wettedPerimeter();
		// Increase water level until either the equation is satisfied or the water overflows the banks
		while (this.calculator.withinBounds() && !this.equation.manningFormulaConsistent())
		{
			this.calculator.setWaterLevel(this.calculator.getWaterLevel().doubleValue() + step);
			// Warning: If water level overflows, a and p will both be zero, invalidating the equation
			this.equation.a = this.calculator.crossSectionArea();
			this.equation.p = this.calculator.wettedPerimeter();
		}

		this.equation.q = null;
		this.equation.a = null;
		this.equation.p = null;
		return this.calculator.getWaterLevel().doubleValue();
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

	public boolean withinBounds(Number level)
	{
		this.calculator.setWaterLevel(level);
		return this.calculator.withinBounds();
	}
}
