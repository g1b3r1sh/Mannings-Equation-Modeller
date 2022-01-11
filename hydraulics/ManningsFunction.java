package hydraulics;

import data.ContinuousFunction;

public class ManningsFunction implements ContinuousFunction
{
	private ManningsModel model;
	private double upperDischargeLimit; // Non-inclusive

	public ManningsFunction(ManningsModel model)
	{
		this.model = model;
		this.upperDischargeLimit = ManningsFunction.calculateUpperLimit(this.model);
	}

	@Override
	public boolean hasY(Double x)
	{
		if (!this.model.calcWaterLevelValid())
		{
			return false;
		}
		return 0 <= x && x < this.upperDischargeLimit;
	}

	@Override
	public Double y(Double x) throws IllegalArgumentException
	{
		if (!this.hasY(x))
		{
			throw new IllegalArgumentException("x is out of range.");
		}

	}
	
	private static double calculateUpperLimit(ManningsModel model)
	{

	}
}
