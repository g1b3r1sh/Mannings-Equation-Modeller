package hydraulics;

import graphs.DiscreteData;

public class WaterLevelCalculator<N extends Number, M extends Number>
{
	private DiscreteData<N, M> sectionData;
	private N waterLevel;

	public WaterLevelCalculator(DiscreteData<N, M> sectionData, N waterLevel)
	{
		this.sectionData = sectionData;
		this.setWaterLevel(waterLevel);
	}

	public N getWaterLevel()
	{
		return waterLevel;
	}

	public void setWaterLevel(N waterLevel)
	{
		this.waterLevel = waterLevel;
	}
	
	public DiscreteData<N, M> getSectionData()
	{
		return this.sectionData;
	}
	
	public boolean withinBounds()
	{
		return this.aboveWater(sectionData.getXSet().first()) && this.aboveWater(sectionData.getXSet().last());
	}

	// Points at water level are considered out of water
	public boolean aboveWater(N x)
	{
		return this.sectionData.y(x).doubleValue() > this.waterLevel.doubleValue();
	}

	// To generate shapes, go through all data
	// If data is below water, add to list
	// If data is above water, list is converted into shape (if it contains data, of course)
}
