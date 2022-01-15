package hydraulics;

import java.util.Map.Entry;

import data.ContinuousFunction;
import data.DiscreteData;

// Function mapping water level to discharge
public class ManningsFunction implements ContinuousFunction
{
	private ManningsModel model;
	private double lowestElevation;
	private double highestElevation; 

	public ManningsFunction(ManningsModel model)
	{
		this.model = model;
		this.lowestElevation = 0;
		this.highestElevation = -1;
		this.updateRange();
	}

	@Override
	public boolean hasY(Double x)
	{
		if (!this.model.isCalcLevelReady() || this.model.getSectionData().size() == 0)
		{
			return false;
		}
		return this.lowestElevation <= x && x < this.highestElevation;
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

	public void updateRange()
	{
		Number lowest = ManningsFunction.getMinLevelY(this.model.getSectionData());
		if (lowest != null)
		{
			this.lowestElevation = lowest.doubleValue();
		}

		Number highest = ManningsFunction.getMaxLevelY(this.model.getSectionData());
		if (highest != null)
		{
			this.highestElevation = highest.doubleValue();
		}
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

	private static <M extends Number, N extends Number> N getMinLevelY(DiscreteData<M, N> data)
	{
		if (data.size() == 0)
		{
			return null;
		}
		N lowest = data.y(data.getXSet().first());
		for (Entry<M, N> e : data.getEntrySet())
		{
			if (e.getValue().doubleValue() < lowest.doubleValue())
			{
				lowest = e.getValue();
			}
		}
		return lowest;
	}
}
