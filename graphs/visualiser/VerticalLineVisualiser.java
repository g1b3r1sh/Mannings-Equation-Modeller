package graphs.visualiser;

import java.awt.Graphics;

import graphs.Graph;
import graphs.GraphComponent;
import graphs.Plane;

public class VerticalLineVisualiser extends GraphComponent
{
	private double x;

	public VerticalLineVisualiser(Graph graph, double x)
	{
		super(graph);
		this.x = x;
	}

	public double getX()
	{
		return this.x;
	}

	public void setX(double x)
	{
		this.x = x;
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		Plane plane = this.getGraph().getPlane();
		g.drawLine(plane.posX(this.x), 0, plane.posX(this.x), this.getHeight());
	}
}
