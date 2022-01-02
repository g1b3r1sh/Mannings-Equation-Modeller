package hydraulics;

import java.util.LinkedList;
import java.util.Map.Entry;

import data.ContinuousData;
import data.DiscreteData;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;

/**
 * Stores water level and calculates hydraulic data based off of it
**/

public class WaterLevelCalculator<M extends Number, N extends Number>
{
	private ContinuousData<M, N> sectionData;
	private Number waterLevel;

	public WaterLevelCalculator(DiscreteData<M, N> sectionData, Number waterLevel)
	{
		this.sectionData = new ContinuousData<M, N>(sectionData);
		this.setWaterLevel(waterLevel);
	}

	public Number getWaterLevel()
	{
		return waterLevel;
	}

	public void setWaterLevel(Number waterLevel)
	{
		this.waterLevel = waterLevel;
	}
	
	public DiscreteData<M, N> getSectionData()
	{
		return this.sectionData.getData();
	}
	
	// Returns whether water is contained within data (i.e. No water is spilling out of left or right side)
	// WARNING: Does not return whether data allows for water to exist 
	public boolean withinBounds()
	{
		if (this.sectionData.getData().size() > 1)
		{
			return this.aboveWater(this.sectionData.getData().getXSet().first()) && this.aboveWater(this.sectionData.getData().getXSet().last());
		}
		else
		{
			return false;
		}
	}

	// Points at water level are considered out of water
	public boolean aboveWater(M x)
	{
		return this.sectionData.getData().y(x).doubleValue() >= this.waterLevel.doubleValue();
	}

	public double crossSectionArea()
	{
		double area = 0;
		for (Path2D.Double poly : this.generateWaterPolygons())
		{
			area += this.calcArea(poly);
		}
		return area;
	}

	// To generate shapes, go through all data
	// If data is below water, add to list
	// If data is above water, list is converted into shape (if it contains data, of course)
	public LinkedList<Path2D.Double> generateWaterPolygons()
	{
		LinkedList<Path2D.Double> list = new LinkedList<>();
			if (this.withinBounds())
			{
				Path2D.Double currentPolygon = null;
				for (Entry<M, N> e : this.sectionData.getData().getEntrySet())
				{
					M x = e.getKey();
					N y = e.getValue();
					if (!this.aboveWater(x))
					{
						if (currentPolygon == null)
						{
							double xPos = this.waterIntersection(x);
							currentPolygon = new Path2D.Double();
							currentPolygon.moveTo(xPos, this.sectionData.y(xPos));
						}
						currentPolygon.lineTo(x.doubleValue(), y.doubleValue());
					}
					else if (this.aboveWater(x) && currentPolygon != null)
					{
						double xPos = this.waterIntersection(x);
						currentPolygon.lineTo(xPos, this.sectionData.y(xPos));
						currentPolygon.closePath();
						list.add(currentPolygon);
						currentPolygon = null;
					}
				}
			}
		return list;
	}

	// Calculate x position of point where water intersects with line between given point and point before it
	private double waterIntersection(M rightX)
	{
		return this.sectionData.xIntersection(this.sectionData.getData().getXSet().lower(rightX), rightX, this.waterLevel);
	}

	// Area between two points makes a sideways trapezoid
	// Therefore, from x0 to x1, A = (y - y0 + y - y1) / 2 * (x1 - x0)
	private double calcArea(Path2D.Double poly)
	{
		double area = 0;
		PathIterator it = poly.getPathIterator(null);
		double[] coordsLeft = new double[6];
		double[] coordsRight = new double[6];
		it.currentSegment(coordsLeft);
		it.next();
		while (it.currentSegment(coordsRight) != PathIterator.SEG_CLOSE)
		{
			area += (this.waterLevel.doubleValue() - coordsLeft[1] + this.waterLevel.doubleValue() - coordsRight[1]) / 2 * (coordsRight[0] - coordsLeft[0]);
			it.currentSegment(coordsLeft);
			it.next();
		}
		return area;
	}
}
