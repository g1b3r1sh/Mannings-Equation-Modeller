package graphs;

public interface ConnectedData<N extends Number, M extends Number> extends DiscreteData<N, M>
{
	public double getSlope(N x1, N x2);
	public double xDouble(N x1, N x2, M y);
}
