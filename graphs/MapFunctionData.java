package graphs;

import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * An instance of DiscreteData through a Map
**/

// TODO: For some reason this also contains precision??
public class MapFunctionData<N extends Number, M extends Number> implements DiscreteData<N, M>
{
	private NavigableMap<N, M> data;
	private int precisionX;
	private int precisionY;

	public MapFunctionData(int precisionX, int precisionY)
	{
		this.precisionX = precisionX;
		this.precisionY = precisionY;
		this.data = new TreeMap<>();
	}

	public MapFunctionData(DiscreteData<N, M> data, int precisionX, int precisionY)
	{
		this(precisionX, precisionY);
		for (N x : data.getXSet())
		{
			this.set(x, data.y(x));
		}
	}

	public int getPrecisionX()
	{
		return precisionX;
	}

	public void setPrecisionX(int precisionX)
	{
		this.precisionX = precisionX;
	}
	
	public int getPrecisionY()
	{
		return precisionY;
	}

	public void setPrecisionY(int precisionY)
	{
		this.precisionY = precisionY;
	}

	@Override
	public void set(N x, M y)
	{
		this.data.put(x, y);
	}

	@Override
	public void remove(N x)
	{
		this.data.remove(x);
	}

	@Override
	public boolean hasY(N x)
	{
		return this.data.containsKey(x);
	}

	@Override
	public double yDouble(Number x)
	{
		return this.data.get(x).doubleValue();
	}

	@Override
	public M y(N x)
	{
		return this.data.get(x);
	}

	@Override
	public int size()
	{
		return this.data.size();
	}

	// Note: Any modifications to set will also modify data
	@Override
	public NavigableSet<N> getXSet()
	{
		return new TreeSet<N>(this.data.keySet());
	}
}