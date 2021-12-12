package graphs;

import java.awt.Graphics;
import java.awt.Color;

public class DataPlotter extends DataVisualiser
{
	private final Color CIRCLE_COLOR = Color.BLACK;

	private int circleSize;

	public DataPlotter(Graph graph, DiscreteData<? extends Number, ? extends Number> data, int circleSize)
	{
		super(graph, data);
		this.circleSize = circleSize;
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(CIRCLE_COLOR);
		Plane plane = this.getGraph().getPlane();
		for (Number x : this.getData().getXSet())
		{
			int xPos = plane.posX(x) - this.circleSize / 2;
			int yPos = plane.posY(this.getData().y(x)) - this.circleSize / 2;
			g.fillOval(xPos, yPos, this.circleSize, this.circleSize);
		}
	}
}