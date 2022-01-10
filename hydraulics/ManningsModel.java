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

	public ManningsModel(DiscreteData<?, ?> data, double n, double s)
	{
		this.calculator = new WaterLevelCalculator<>(data, 0);
		this.equation = new ManningsEquation();
		this.equation.n = n;
		this.equation.s = s;
	}

	// If there does not exist a water level that satisfies the equation, will return a water level that is out of bounds of data
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

	public boolean withinBounds(Number level)
	{
		this.calculator.setWaterLevel(level);
		return this.calculator.withinBounds();
	}
}
