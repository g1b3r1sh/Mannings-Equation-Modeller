package graphs.visualiser;

import java.awt.Color;
import java.awt.Graphics;

import data.functions.ContinuousFunction;
import graphs.Graph;
import graphs.GraphComponent;
import graphs.Plane;

public class ContinuousFunctionVisualiser extends GraphComponent
{
	private static final int PIXEL_STEP = 1;

	private ContinuousFunction function;
	private Color color = Color.BLACK;

	public ContinuousFunctionVisualiser(Graph graph, ContinuousFunction function)
	{
		super(graph);
		this.function = function;
	}

	public ContinuousFunction getFunction()
	{
		return this.function;
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(this.color);
		Plane plane = this.getGraph().getPlane();
		int pixelStep = ContinuousFunctionVisualiser.PIXEL_STEP;
		this.paintLine(g, plane, pixelStep);
	}

	protected void paintLine(Graphics g, Plane plane, int pixelStep)
	{
		for (int screenX = 0; screenX <= this.getGraph().getCanvasWidth(); screenX += pixelStep)
		{
			if (this.function.hasY(plane.inversePosX(screenX)) && this.function.hasY(plane.inversePosX(screenX + pixelStep)))
			{
				int x1 = screenX;
				int y1 = plane.posY(this.function.y(plane.inversePosX(x1)));
				int x2 = screenX + pixelStep;
				int y2 = plane.posY(this.function.y(plane.inversePosX(x2)));
				g.drawLine(x1, y1, x2, y2);
			}
		}
	}
}
