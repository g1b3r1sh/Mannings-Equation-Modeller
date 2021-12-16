package hydraulics;

import java.util.LinkedList;
import java.awt.geom.Path2D;

import graphs.DiscreteData;
import graphs.MapFunctionDataConnected;

public class WaterLevelCalculator<N extends Number, M extends Number>
{
	private MapFunctionDataConnected<N, M> sectionData;
	private M waterLevel;

	public WaterLevelCalculator(DiscreteData<N, M> sectionData, M waterLevel)
	{
		this.sectionData = new MapFunctionDataConnected<N, M>(sectionData);
		this.setWaterLevel(waterLevel);
	}

	public M getWaterLevel()
	{
		return waterLevel;
	}

	public void setWaterLevel(M waterLevel)
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
		return this.sectionData.y(x).doubleValue() >= this.waterLevel.doubleValue();
	}

	// To generate shapes, go through all data
	// If data is below water, add to list
	// If data is above water, list is converted into shape (if it contains data, of course)
	// Assumes that withinBounds() is true
	public LinkedList<Path2D.Double> generateWaterPolygons()
	{
		LinkedList<Path2D.Double> list = new LinkedList<>();
		Path2D.Double currentPolygon = null;
		for (N x : this.sectionData.getXSet())
		{
			if (!this.aboveWater(x))
			{
				if (currentPolygon == null)
				{
					double xPos = this.waterIntersection(x);
					currentPolygon = new Path2D.Double();
					currentPolygon.moveTo(xPos, this.sectionData.yDouble(xPos));
				}
				currentPolygon.lineTo(x.doubleValue(), this.sectionData.y(x).doubleValue());
			}
			else if (this.aboveWater(x) && currentPolygon != null)
			{
				double xPos = this.waterIntersection(x);
				currentPolygon.lineTo(xPos, this.sectionData.yDouble(xPos));
				currentPolygon.closePath();
				list.add(currentPolygon);
				currentPolygon = null;
			}
		}
		return list;
	}

	// Calculate x position of point where water intersects with line between given point and point before it
	private double waterIntersection(N rightX)
	{
		return this.sectionData.xDouble(this.sectionData.getXSet().lower(rightX), rightX, this.waterLevel);
	}

	// TODO
	/*private double calcArea(Path2D.Double poly)
	{
		
	}*/
}
