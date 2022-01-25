package data.functions;

import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * An implementation of DiscreteData using TreeMap
**/

public class MapDiscreteData<M extends Number, N extends Number> implements DiscreteData<M, N>
{
	private NavigableMap<M, N> data;

	public MapDiscreteData()
	{
		this.data = new TreeMap<>();
	}

	public MapDiscreteData(DiscreteData<M, N> data)
	{
		this();
		this.load(data);
	}

	@Override
	public void set(M x, N y)
	{
		this.data.put(x, y);
	}

	@Override
	public void remove(M x)
	{
		this.data.remove(x);
	}

	@Override
	public boolean hasY(M x)
	{
		return this.data.containsKey(x);
	}

	@Override
	public N y(M x)
	{
		return this.data.get(x);
	}

	@Override
	public void clear()
	{
		this.data.clear();
	}

	@Override
	public void load(DiscreteData<M, N> data)
	{
		this.clear();
		for (M x : data.getXSet())
		{
			this.set(x, data.y(x));
		}
	}

	@Override
	public int size()
	{
		return this.data.size();
	}

	// Note: Any modifications to sets will also modify data
	@Override
	public NavigableSet<M> getXSet()
	{
		return new TreeSet<M>(this.data.keySet());
	}

	@Override
	public Set<Entry<M, N>> getEntrySet()
	{
		return this.data.entrySet();
	}
}