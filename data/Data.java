package data;

/**
 * Represents a mathematical function. Contains methods for converting into double.
**/

public interface Data<N extends Number, M extends Number>
{
	public boolean hasY(N x);
	public double yDouble(Number x);
	public M y(N x);
}