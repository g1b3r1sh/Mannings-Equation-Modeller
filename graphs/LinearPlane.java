package graphs;

/**
 * An instance of Plane where the scales are linear (i.e. Numbers scale up in increments rather than by logs or multiples)
**/

// Does not have any visual, essentially is a mathematical function (pass numbers, return numbers)
// Plane that uses linear scale
public class LinearPlane extends Plane
{
	public LinearPlane(Graph graph, Range rangeX, Range rangeY)
	{
		super(graph, rangeX, rangeY);
	}

	@Override
	public int posX(double x)
	{
		return (int) (this.cellWidth() * (x - this.getRangeX().getLower()));
	}

	@Override
	public int posY(double y)
	{
		return (int) (this.cellHeight() * this.invertY(y - this.getRangeY().getLower()));
	}
	
	@Override
	public double inversePosX(int x)
	{
		return ((double) x) / this.cellWidth() + this.getRangeX().getLower();
	}

	@Override
	public double inversePosY(int y)
	{
		return (((double) y) / this.cellHeight() - this.getRangeY().size() - this.getRangeY().getLower()) * -1;
	}

	// Since grid y is normally up-down, invertY makes it down-up
	private double invertY(double y)
	{
		return this.getRangeY().size() - y;
	}

	private double cellWidth()
	{
		return (double) this.getWidth() / this.getRangeX().size();
	}

	private double cellHeight()
	{
		return (double) this.getHeight() / this.getRangeY().size();
	}
}