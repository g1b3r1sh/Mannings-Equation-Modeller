package data.functions;

import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;

/**
 * Represents a set of discrete data coordinates
**/

public interface DiscreteData<M extends Number, N extends Number> extends Function<M, N>
{
	public void set(M x, N y);
	public void remove(M x);
	public void clear();
	public void load(DiscreteData<M, N> data);
	public int size();
	// Returns ordered set of x values
	public NavigableSet<M> getXSet();
	public Set<Map.Entry<M, N>> getEntrySet();
}