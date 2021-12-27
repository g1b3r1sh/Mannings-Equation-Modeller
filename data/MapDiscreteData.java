package data;

import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * An instance of DiscreteData through a Map
**/

// TODO: For some reason this also contains precision??
public class MapDiscreteData<N extends Number, M extends Number> implements DiscreteData<N, M>
{
	private NavigableMap<N, M> data;

	public MapDiscreteData()
	{
		this.data = new TreeMap<>();
	}

	public MapDiscreteData(DiscreteData<N, M> data)
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

	@Override
	public Set<Entry<N, M>> getEntrySet()
	{
		return this.data.entrySet();
	}
}