package data;

import java.util.Iterator;

/**
 * Represent continuous function constructed by connecting all points in DiscreteData
**/

public class ContinuousData<M extends Number, N extends Number> implements FunctionData
{
	private DiscreteData<M, N> data;

	public ContinuousData(DiscreteData<M, N> data)
	{
		this.data = data;
	}

	public DiscreteData<M, N> getData()
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
	
	// Slope equation
	public double getSlope(M x1, M x2)
	{
		return (this.data.y(x2).doubleValue() - this.data.y(x1).doubleValue()) / (x2.doubleValue() - x1.doubleValue());
	}

	// Returns point x of intersection between line created by two points and flat line
	// Point form equation
	public double xDouble(M x1, M x2, Number y)
	{
		return ((y.doubleValue() - this.data.y(x1).doubleValue()) / this.getSlope(x1, x2)) + x1.doubleValue();
	}
}
