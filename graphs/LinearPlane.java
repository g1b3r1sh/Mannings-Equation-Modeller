package graphs;

// Does not have any visual, essentially is a mathematical function (pass numbers, return numbers)
// Plane that uses linear scale
public class LinearPlane extends Plane
{
	private Range rangeX;
	private Range rangeY;

	public LinearPlane(Graph graph, Range rangeX, Range rangeY)
	{
		super(graph, rangeX, rangeY);
	}

	@Override
	public int posX(double x)
	{
		return (int) (this.cellWidth()  * (x - this.rangeX.getLower()));
	}

	@Override
	public int posY(double y)
	{
		return (int) (this.cellHeight() * this.invertY(y - this.rangeY.getLower()));
	}

	// Since grid y is normally up-down, invertY makes it down-up
	private double invertY(double y)
	{
		return this.rangeY.size() - y;
	}

	private double cellWidth()
	{
		return (double) this.getWidth() / this.rangeX.size();
	}

	private double cellHeight()
	{
		return (double) this.getHeight() / this.rangeY.size();
	}
}