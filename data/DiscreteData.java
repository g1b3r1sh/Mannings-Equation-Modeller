package data;

import java.util.NavigableSet;

/**
 * Represents a discrete data function
**/

public interface DiscreteData<N extends Number, M extends Number> extends Data<N, M>, DataInformation
{
	public void set(N x, M y);
	public void remove(N x);
	public int size();
	// Returns ordered set of x values
	public NavigableSet<N> getXSet();
}