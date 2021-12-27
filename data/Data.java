package data;

/**
 * Represents a mathematical function. Contains methods for converting into double.
**/

public interface Data<M extends Number, N extends Number>
{
	public boolean hasY(M x);
	public N y(M x) throws IllegalArgumentException;
}