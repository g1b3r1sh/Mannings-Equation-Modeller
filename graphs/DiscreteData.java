package graphs;

import java.util.SortedSet;

public interface DiscreteData<N extends Number, M extends Number> extends FunctionData<N, M>
{
	public void set(N x, M y);
	public void remove(N x);
	// Returns ordered set of x values
	public SortedSet<N> getXSet();
}