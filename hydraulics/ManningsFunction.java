package hydraulics;

import java.util.Map.Entry;

import data.functions.ContinuousFunction;
import data.functions.DiscreteData;

// Function mapping water level to discharge
public class ManningsFunction implements ContinuousFunction
{
	private ManningsModel model;
	private double lowestElevation;

	public ManningsFunction(ManningsModel model)
	{
		this.model = model;
		this.lowestElevation = 0;
		this.updateRange();
	}

	@Override
	public boolean hasY(Double x)
	{
		if (!this.model.areConstantsSet() || this.model.getSectionData().size() < 2)
		{
			return false;
		}
		return x >= this.lowestElevation;
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

	public void update()
	{
		this.updateRange();
	}

	private void updateRange()
	{
		Number lowest = ManningsFunction.getMinLevelY(this.model.getSectionData());
		if (lowest != null)
		{
			this.lowestElevation = lowest.doubleValue();
		}
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
