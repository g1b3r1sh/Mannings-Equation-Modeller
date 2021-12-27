package data;

import java.util.NavigableSet;

/**
 * Represents a discrete data function
**/

public interface DiscreteData<M extends Number, N extends Number> extends Data<M, N>, DataInformation
{
	public void set(M x, N y);
	public void remove(M x);
	public int size();
	// Returns ordered set of x values
	public NavigableSet<M> getXSet();
}