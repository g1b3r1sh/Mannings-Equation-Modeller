package graphs;

import java.awt.Graphics;

/**
 * Represents the plane of a graph. Contains methods for retrieving graphic positions of points from points inside the plane.
**/

public abstract class Plane extends GraphComponent 
{
	private Range rangeX;
	private Range rangeY;

	public Plane(Graph graph, Range rangeX, Range rangeY)
	{
		super(graph);
		this.rangeX = rangeX;
		this.rangeY = rangeY;
	}

	public Range getRangeX()
	{
		return this.rangeX;
	}

	public Range getRangeY()
	{
		return this.rangeY;
	}

	public abstract int posX(double x);
	public abstract int posY(double y);

	public int posX(Number x)
	{
		return this.posX(x.doubleValue());
	}

	public int posY(Number y)
	{
		return this.posY(y.doubleValue());
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		return;
	}
}