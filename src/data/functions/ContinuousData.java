package data.functions;

import java.util.Iterator;
import java.util.NavigableSet;
import java.util.function.BiConsumer;

/**
 * Represents continuous function constructed by connecting all points in DiscreteData
**/

public class ContinuousData<M extends Number, N extends Number> implements ContinuousFunction
{
	private DiscreteData<M, N> data;

	public ContinuousData(DiscreteData<M, N> data)
	{
		this.data = data;
	}

	public DiscreteData<M, N> getDataSet()
	{
		return this.data;
	}

	@Override
	public boolean hasY(Double x)
	{
		return this.data.getXSet().first().doubleValue() <= x.doubleValue() && x.doubleValue() <= this.data.getXSet().last().doubleValue();
	}

	@Override
	public Double y(Double x) throws IllegalArgumentException
	{
		if (this.data.size() == 0)
		{
			throw new IllegalArgumentException("Data is empty");
		}
		else if (this.data.size() == 1)
		{
			if (x.doubleValue() == this.data.getXSet().first().doubleValue())
			{
				return this.data.getXSet().first().doubleValue();
			}
			else
			{
				throw new IllegalArgumentException("Number is out of bounds of data");
			}
		}
		Iterator<M> it = this.data.getXSet().iterator();
		M pointX = it.next();
		if (pointX.doubleValue() > x.doubleValue())
		{
			throw new IllegalArgumentException("Number is lower than bounds of data.");
		}
		while (it.hasNext())
		{
			pointX = it.next();
			if (pointX.doubleValue() >= x.doubleValue())
			{
				return ((x.doubleValue() - pointX.doubleValue()) * this.getSlope(pointX, this.data.getXSet().lower(pointX))) + this.data.y(pointX).doubleValue();
			}
		}
		throw new IllegalArgumentException("Number is higher than bounds of data.");
	}

	// Slope equation for line between two data points
	public double getSlope(M x1, M x2)
	{
		return (this.data.y(x2).doubleValue() - this.data.y(x1).doubleValue()) / (x2.doubleValue() - x1.doubleValue());
	}

	// Length of segment between two points
	public double segmentLength(double x1, double x2)
	{
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(this.y(x1) - this.y(x2), 2));
	}

	// Passes left and right x-value of each line segments in data in order of left to right to the operation
	public void iterateSegments(BiConsumer<M, M> operation)
	{
		NavigableSet<M> xSet = this.data.getXSet();
		if (xSet.size() > 1)
		{
			Iterator<M> itRight = xSet.iterator();
			itRight.next();
			while (itRight.hasNext())
			{
				M right = itRight.next();
				operation.accept(xSet.lower(right), right);
			}
		}
	}
}
