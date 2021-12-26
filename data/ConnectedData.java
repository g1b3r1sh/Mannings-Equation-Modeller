package data;

/**
 * Contains additional methods to represent continuous function constructed by connecting all points in DiscreteData
**/

public interface ConnectedData<N extends Number, M extends Number> extends DiscreteData<N, M>
{
	public double getSlope(N x1, N x2);
	public double xDouble(N x1, N x2, Number y);
}
