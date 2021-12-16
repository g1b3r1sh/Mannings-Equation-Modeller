package graphs;

import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class MapFunctionData<N extends Number, M extends Number> implements DiscreteData<N, M>
{
	private SortedMap<N, M> data;

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
	
	public void set(N x, M y)
	{
		this.data.put(x, y);
	}

	public void remove(N x)
	{
		this.data.remove(x);
	}

	public boolean hasY(N x)
	{
		return this.data.containsKey(x);
	}

	public double yDouble(Number x)
	{
		return this.data.get(x).doubleValue();
	}

	public M y(N x)
	{
		return this.data.get(x);
	}

	// Note: Any modifications to set will also modify data
	public SortedSet<N> getXSet()
	{
		return new TreeSet<N>(this.data.keySet());
	}

	protected SortedMap<N, M> getMap()
	{
		return this.data;
	}
}