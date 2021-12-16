package graphs;

import java.util.Iterator;

// Continuous function for connected data, inclusive
public class MapFunctionDataConnected<N extends Number, M extends Number> extends MapFunctionData<N, M>
{
	public MapFunctionDataConnected(DiscreteData<N, M> data)
	{
		super(data);
	}

	@Override
	public boolean hasY(N x)
	{
		return this.getXSet().first().doubleValue() <= x.doubleValue() && x.doubleValue() <= this.getXSet().last().doubleValue();
	}

	@Override
	public double yDouble(Number x)
	{
		Iterator<N> it = this.getXSet().iterator();
		N pointX = it.next();
		if (pointX.doubleValue() > x.doubleValue())
		{
			throw new IllegalArgumentException("Number x is not within bounds of data.");
		}
		while (it.hasNext())
		{
			pointX = it.next();
			if (pointX.doubleValue() >= x.doubleValue())
			{
				return ((x.doubleValue() - pointX.doubleValue()) * this.getSlope(pointX, this.getXSet().lower(pointX))) + this.y(pointX).doubleValue();
			}
		}
		throw new IllegalArgumentException("Number x is not within bounds of data.");
	}

	// Slope equation
	public double getSlope(N x1, N x2)
	{
		return (this.y(x2).doubleValue() - this.y(x1).doubleValue()) / (x2.doubleValue() - x1.doubleValue());
	}

	// Returns point x of intersection between line created by two points and flat line
	// Point form equation
	public double xDouble(N x1, N x2, M y)
	{
		return ((y.doubleValue() - this.y(x1).doubleValue()) / this.getSlope(x1, x2)) + x1.doubleValue();
	}
}