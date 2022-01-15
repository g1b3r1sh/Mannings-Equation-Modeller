package hydraulics;

import data.ContinuousFunction;
import data.DiscreteData;

// Function mapping water level to discharge
public class ManningsFunction implements ContinuousFunction
{
	private ManningsModel model;
	private double upperDischargeLimit; 

	public ManningsFunction(ManningsModel model)
	{
		this.model = model;
		this.upperDischargeLimit = 0;
	}

	@Override
	public boolean hasY(Double x)
	{
		if (!this.model.isCalcLevelReady())
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
		return this.model.calcDischarge(x);
	}

	public void updateUpperLimit()
	{
		if (this.model.isCalcLevelReady())
		{
			this.upperDischargeLimit = this.getUpperLimit();
		}
	}

	// Non-inclusive
	private double getUpperLimit()
	{
		DiscreteData<?, ?> data = this.model.getSectionData();
		if (data.size() == 0)
		{
			return 0;
		}
		return this.model.calcDischarge(ManningsFunction.getMaxLevelY(data).doubleValue());
	}

	private static <M extends Number, N extends Number> N getMaxLevelY(DiscreteData<M, N> data)
	{
		if (data.size() == 0)
		{
			return null;
		}
		N left = data.y(data.getXSet().first());
		N right = data.y(data.getXSet().last());
		return left.doubleValue() < right.doubleValue() ? left : right;
	}
}
