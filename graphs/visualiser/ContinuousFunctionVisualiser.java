package graphs.visualiser;

import java.awt.Color;
import java.awt.Graphics;

import data.ContinuousFunction;
import graphs.Graph;
import graphs.GraphComponent;
import graphs.Plane;

public class ContinuousFunctionVisualiser extends GraphComponent
{
	private static final int PIXEL_STEP = 5;

	private ContinuousFunction function;

	public ContinuousFunctionVisualiser(Graph graph, ContinuousFunction function)
	{
		super(graph);
		this.function = function;
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(Color.BLACK);
		Plane plane = this.getGraph().getPlane();
		int step = ContinuousFunctionVisualiser.PIXEL_STEP;
		for (int screenX = 0; screenX <= this.getGraph().getCanvasWidth(); screenX += step)
		{
			if (this.function.hasY(plane.inversePosX(screenX)) && this.function.hasY(plane.inversePosX(screenX + step)))
			{
				g.drawLine(screenX, plane.posY(this.function.y(plane.inversePosX(screenX))), screenX + step, plane.posY(this.function.y(plane.inversePosX(screenX + step))));
			}
		}
	}
}
