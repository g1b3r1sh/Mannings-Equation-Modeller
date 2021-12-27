package data;

import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;

/**
 * Represents a discrete data function
**/

public interface DiscreteData<M extends Number, N extends Number> extends Data<M, N>
{
	public void set(M x, N y);
	public void remove(M x);
	public int size();
	// Returns ordered set of x values
	public NavigableSet<M> getXSet();
	public Set<Map.Entry<M, N>> getEntrySet();
}