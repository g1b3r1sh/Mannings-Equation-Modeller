package hydraulics;

import java.util.LinkedList;
import java.util.Map.Entry;

import data.functions.ContinuousData;
import data.functions.DiscreteData;
import utility.Wrapper;

import java.awt.geom.Path2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Stores water level and calculates hydraulic data based off of it
**/

public class WaterLevelCalculator<M extends Number, N extends Number>
{
	private ContinuousData<M, N> data;
	private Number waterLevel;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	// Enum for placement of points in line segment in relation to water level
	private enum SegmentPlacement
	{
		LEFT_ABOVE, RIGHT_ABOVE, NONE_ABOVE, BOTH_ABOVE
	}

	public WaterLevelCalculator(DiscreteData<M, N> data, Number waterLevel)
	{
		if (waterLevel == null)
		{
			throw new IllegalArgumentException("Water level cannot be null");
		}
		this.data = new ContinuousData<M, N>(data);
		this.waterLevel = waterLevel;
	}

	public Number getWaterLevel()
	{
		return this.waterLevel;
	}

	public void setWaterLevel(Number waterLevel)
	{
		if (waterLevel != null)
		{
			Number oldValue = this.waterLevel;
			this.waterLevel = waterLevel;
			this.propertyChangeSupport.firePropertyChange("waterLevel", oldValue, waterLevel);
		}
	}

	public DiscreteData<M, N> getSectionData()
	{
		return this.data.getDataSet();
	}

	// Points at water level are considered out of water
	// Assumes that x is a point in the dataset
	public boolean aboveWater(M x)
	{
		return this.data.getDataSet().y(x).doubleValue() >= this.waterLevel.doubleValue();
	}

	// Returns lowest possble water level (without it not touching the continuous function of the data points)
	public N getLowest()
	{
		DiscreteData<M, N> dataset = this.data.getDataSet();
		if (dataset.size() > 0)
		{
			N lowest = dataset.y(dataset.getXSet().first());
			for (Entry<M, N> e : dataset.getEntrySet())
			{
				if (e.getValue().doubleValue() < lowest.doubleValue())
				{
					lowest = e.getValue();
				}
			}
			return lowest;
		}
		return null;
	}

	public void moveToLowest()
	{
		this.setWaterLevel(this.getLowest());
	}

	public double crossSectionArea()
	{
		// Data must be wrapped in Wrapper to be passed to lambda function
		Wrapper<Double> area = new Wrapper<>(0d);
		this.data.iterateSegments( (left, right) -> area.value += WaterLevelCalculator.this.calcArea(left, right) );
		return area.value;
	}

	public double wettedPerimeter()
	{
		// Data must be wrapped in Wrapper to be passed to lambda function
		Wrapper<Double> perimeter = new Wrapper<>(0d);
		this.data.iterateSegments
		(
			(left, right) ->
			{
				switch (this.getPlacement(left, right))
				{
					case LEFT_ABOVE:
						perimeter.value += this.data.segmentLength(this.waterIntersectionX(left, right), right.doubleValue());
						break;
					case RIGHT_ABOVE:
						perimeter.value += this.data.segmentLength(left.doubleValue(), this.waterIntersectionX(left, right));
						break;
					case NONE_ABOVE:
						perimeter.value += this.data.segmentLength(left.doubleValue(), right.doubleValue());
						break;
					case BOTH_ABOVE:
					default:
						break;
				}
			}
		);
		return perimeter.value;
	}

	// Returns point x of intersection between line created by two data points and water level
	// Point form equation: Given P(x1, y1), y, and m, where y - y1 = m(x - x1), x is calculated as x = (y - y1) / m + x1
	private double waterIntersectionX(M x1, M x2)
	{
		double y = this.waterLevel.doubleValue();
		double y1 = this.getSectionData().y(x1).doubleValue();
		return ((y - y1) / this.data.getSlope(x1, x2)) + x1.doubleValue();
	}

	private double calcArea(M leftX, M rightX)
	{
		N leftY = this.getSectionData().y(leftX);
		N rightY = this.getSectionData().y(rightX);
		double waterLevelY = this.waterLevel.doubleValue();
		switch (this.getPlacement(leftX, rightX))
		{
			case LEFT_ABOVE:
				return this.calcSegmentArea(this.waterIntersectionX(leftX, rightX), waterLevelY, rightX.doubleValue(), rightY.doubleValue());
			case RIGHT_ABOVE:
				return this.calcSegmentArea(leftX.doubleValue(), leftY.doubleValue(), this.waterIntersectionX(leftX, rightX), waterLevelY);
			case NONE_ABOVE:
				return this.calcSegmentArea(leftX.doubleValue(), leftY.doubleValue(), rightX.doubleValue(), rightY.doubleValue());
			case BOTH_ABOVE:
			default:
				return 0;
		}
	}

	// Area from water level to line segment created by two points makes a sideways trapezoid
	// Therefore, from line segment P0(x0, y0) to P1(x1, y1) and where y = water level, 
	// 		A = (y - y0 + y - y1) / 2 * (x1 - x0)
	// This method assumes that leftY, rightY <= water level and that leftX <= rightX
	private double calcSegmentArea(double leftX, double leftY, double rightX, double rightY)
	{
		return (this.waterLevel.doubleValue() - leftY + this.waterLevel.doubleValue() - rightY) / 2 * (rightX - leftX);
	}

	private SegmentPlacement getPlacement(M leftX, M rightX)
	{
		if (this.aboveWater(leftX) && !this.aboveWater(rightX))
		{
			return SegmentPlacement.LEFT_ABOVE;
		}
		else if (!this.aboveWater(leftX) && this.aboveWater(rightX))
		{
			return SegmentPlacement.RIGHT_ABOVE;
		}
		else if (this.aboveWater(leftX) && this.aboveWater(rightX))
		{
			return SegmentPlacement.BOTH_ABOVE;
		}
		else // if(!this.aboveWater(leftX) && !this.aboveWater(rightX))
		{
			return SegmentPlacement.NONE_ABOVE;
		}
	}

	// To generate shapes, go through all data
	// If data is below water, add to list
	// If data is above water, list is converted into shape (if it contains data, of course)
	public LinkedList<Path2D.Double> generateWaterPolygons()
	{
		LinkedList<Path2D.Double> list = new LinkedList<>();
		Wrapper<Path2D.Double> currentPolygon = new Wrapper<>(null);
		this.data.iterateSegments
		(
			(leftX, rightX) ->
			{
				N leftY = this.getSectionData().y(leftX);
				N rightY = this.getSectionData().y(rightX);
				double intersectX;
				double intersectY;

				switch (this.getPlacement(leftX, rightX))
				{
					case LEFT_ABOVE:
						intersectX = this.waterIntersectionX(leftX, rightX);
						intersectY = this.data.y(intersectX);

						currentPolygon.value = new Path2D.Double();
						currentPolygon.value.moveTo(intersectX, intersectY);
						currentPolygon.value.lineTo(rightX.doubleValue(), rightY.doubleValue());
						break;
					case RIGHT_ABOVE:
						intersectX = this.waterIntersectionX(leftX, rightX);
						intersectY = this.data.y(intersectX);

						if (currentPolygon.value == null)
						{
							currentPolygon.value = new Path2D.Double();
							currentPolygon.value.moveTo(leftX.doubleValue(), this.waterLevel.doubleValue());
							currentPolygon.value.lineTo(leftX.doubleValue(), leftY.doubleValue());
						}
						currentPolygon.value.lineTo(intersectX, intersectY);
						currentPolygon.value.closePath();
						list.add(currentPolygon.value);
						currentPolygon.value = null;
						break;
					case NONE_ABOVE:
						if (currentPolygon.value == null)
						{
							currentPolygon.value = new Path2D.Double();
							currentPolygon.value.moveTo(leftX.doubleValue(), this.waterLevel.doubleValue());
							currentPolygon.value.lineTo(leftX.doubleValue(), leftY.doubleValue());
						}
						currentPolygon.value.lineTo(rightX.doubleValue(), rightY.doubleValue());
						break;
					case BOTH_ABOVE:
					default:
						break;
				}
			}
		);
		if (currentPolygon.value != null)
		{
			currentPolygon.value.lineTo(this.getSectionData().getXSet().last().doubleValue(), this.waterLevel.doubleValue());
			currentPolygon.value.closePath();
			list.add(currentPolygon.value);
		}
		return list;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		this.propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		this.propertyChangeSupport.removePropertyChangeListener(listener);
	}
}
