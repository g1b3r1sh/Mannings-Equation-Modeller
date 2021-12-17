package hydraulics;

import java.util.LinkedList;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;

import graphs.ConnectedData;
import graphs.DiscreteData;

/**
 * Stores water level and calculates hydraulic data based off of it
**/

public class WaterLevelCalculator<N extends Number, M extends Number>
{
	private ConnectedData<N, M> sectionData;
	private Number waterLevel;

	public WaterLevelCalculator(ConnectedData<N, M> sectionData, Number waterLevel)
	{
		this.sectionData = sectionData;
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
	
	public DiscreteData<N, M> getSectionData()
	{
		return this.sectionData;
	}
	
	public boolean withinBounds()
	{
		if (this.sectionData.size() > 1)
		{
			return this.aboveWater(this.sectionData.getXSet().first()) && this.aboveWater(this.sectionData.getXSet().last());
		}
		else
		{
			return false;
		}
	}

	// Points at water level are considered out of water
	public boolean aboveWater(N x)
	{
		return this.sectionData.y(x).doubleValue() >= this.waterLevel.doubleValue();
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
			}
		return list;
	}

	// Calculate x position of point where water intersects with line between given point and point before it
	private double waterIntersection(N rightX)
	{
		return this.sectionData.xDouble(this.sectionData.getXSet().lower(rightX), rightX, this.waterLevel);
	}

	// Take advantage of the fact that water level is constant - area between two points makes a sideways trapezoid
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
