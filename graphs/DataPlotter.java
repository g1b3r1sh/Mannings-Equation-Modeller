package graphs;

import java.awt.Graphics;
import java.awt.Color;

public class DataPlotter<N extends Number, M extends Number> extends GraphComponent
{
	private Color CIRCLE_COLOR = Color.BLACK;

	private DiscreteData<N, M> data;
	private int circleSize;

	public DataPlotter(Graph graph, DiscreteData<N, M> data, int circleSize)
	{
		super(graph);

		this.data = data;
		this.circleSize = circleSize;
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		if (this.data != null)
		{
			g.setColor(CIRCLE_COLOR);
			Plane plane = this.getGraph().getPlane();
			for (N x : this.data.getXSet())
			{
				int xPos = plane.posX(x.doubleValue()) - this.circleSize / 2;
				int yPos = plane.posY(this.data.y(x).doubleValue()) - this.circleSize / 2;
				g.fillOval(xPos, yPos, this.circleSize, this.circleSize);
			}
		}
	}
}