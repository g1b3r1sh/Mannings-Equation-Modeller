package graphs;

/**
 * Represents a mathematical function. Contains methods for converting into double.
**/

public interface FunctionData<N extends Number, M extends Number>
{
	public boolean hasY(N x);
	public double yDouble(Number x);
	public M y(N x);
}