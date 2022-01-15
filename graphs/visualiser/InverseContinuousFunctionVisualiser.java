package graphs.visualiser;

import java.awt.Graphics;

import data.ContinuousFunction;
import graphs.Graph;
import graphs.Plane;

// Draws a function but with inverted values (i.e. (x, y) -> (y, x))
public class InverseContinuousFunctionVisualiser extends ContinuousFunctionVisualiser
{
	public InverseContinuousFunctionVisualiser(Graph graph, ContinuousFunction function)
	{
		super(graph, function);
	}

	@Override
	protected void paintLine(Graphics g, Plane plane, int pixelStep)
	{
		for (int screenY = 0; screenY <= this.getGraph().getCanvasHeight(); screenY += pixelStep)
		{
			if (this.getFunction().hasY(plane.inversePosY(screenY)) && this.getFunction().hasY(plane.inversePosY(screenY + pixelStep)))
			{
				int y1 = screenY;
				int x1 = plane.posX(this.getFunction().y(plane.inversePosY(y1)));
				int y2 = screenY + pixelStep;
				int x2 = plane.posX(this.getFunction().y(plane.inversePosY(y2)));
				g.drawLine(x1, y1, x2, y2);
			}
		}
	}
}
