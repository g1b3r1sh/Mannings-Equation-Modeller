package graphs;

import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class MapFunctionData<N extends Number, M extends Number> implements DiscreteData<N, M>
{
	private NavigableMap<N, M> data;

	public MapFunctionData()
	{
		this.data = new TreeMap<>();
	}

	public MapFunctionData(DiscreteData<N, M> data)
	{
		this();
		for (N x : data.getXSet())
		{
			this.set(x, data.y(x));
		}
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

	// Note: Any modifications to set will also modify data
	@Override
	public NavigableSet<N> getXSet()
	{
		return new TreeSet<N>(this.data.keySet());
	}
}