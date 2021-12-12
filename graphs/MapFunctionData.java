package graphs;

import java.util.Set;
import java.util.Map;
import java.util.TreeMap;

public class MapFunctionData<N extends Number, M extends Number> implements DiscreteData<N, M>
{
	private Map<N, M> data;

	public MapFunctionData()
	{
		this.data = new TreeMap<>();
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

	public M y(Number x)
	{
		return this.data.get(x);
	}

	// Note: Any modifications to set will also modify data
	public Set<N> getXSet()
	{
		return this.data.keySet();
	}
}