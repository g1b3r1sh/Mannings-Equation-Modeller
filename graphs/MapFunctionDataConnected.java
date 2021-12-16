package graphs;

import java.util.Iterator;
import java.util.Map;

// Continuous function for connected data, inclusive
public class MapFunctionDataConnected<N extends Number, M extends Number> extends MapFunctionData<N, M>
{
	@Override
	public boolean hasY(N x)
	{
		return this.getMap().firstKey().doubleValue() <= x.doubleValue() && x.doubleValue() <= this.getMap().lastKey().doubleValue();
	}

	@Override
	public double yDouble(Number x)
	{
		Iterator<Map.Entry<N, M>> itFront = this.getMap().entrySet().iterator();
		Map.Entry<N, M> first = itFront.next();
		if (first.getKey().doubleValue() > x.doubleValue())
		{
			throw new IllegalArgumentException("Number x is not within bounds of data.");
		}
		Iterator<Map.Entry<N, M>> itBack = this.getMap().entrySet().iterator();
		while (itFront.hasNext())
		{
			Map.Entry<N, M> front = itFront.next();
			Map.Entry<N, M> back = itBack.next();
			if (front.getKey().doubleValue() <= x.doubleValue())
			{
				return (x.doubleValue() - front.getKey().doubleValue()) / this.getSlope(front.getKey(), back.getKey()) + front.getValue().doubleValue();
			}
		}
		throw new IllegalArgumentException("Number x is not within bounds of data.");
	}

	public double getSlope(N x1, N x2)
	{
		return (this.y(x2).doubleValue() - this.y(x1).doubleValue()) / (x2.doubleValue() - x1.doubleValue());
	}
}
