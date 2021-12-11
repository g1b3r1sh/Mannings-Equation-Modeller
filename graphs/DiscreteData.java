package graphs;

import java.util.Set;

public interface DiscreteData<N extends Number, M extends Number> extends FunctionData<N>
{
	public void set(N x, M y);
	public void remove(N x);
	public Set<N> getXSet();
}