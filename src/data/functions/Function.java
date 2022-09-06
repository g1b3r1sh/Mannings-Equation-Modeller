package data.functions;

/**
 * Represents a mathematical function
**/  

public interface Function<M extends Number, N extends Number>
{
	public boolean hasY(M x);
	public N y(M x) throws IllegalArgumentException;
}