package graphs;

public interface FunctionData<N extends Number, M extends Number>
{
	public boolean hasY(N x);
	public M y(Number x);
}