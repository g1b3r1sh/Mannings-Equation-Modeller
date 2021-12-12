package graphs;

import java.awt.Graphics;

public abstract class Plane extends GraphComponent 
{
	public Plane(Graph graph)
	{
		super(graph);
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