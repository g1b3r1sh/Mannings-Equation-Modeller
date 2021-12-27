package graphs;

import java.awt.Graphics;

import data.DiscreteData;

/**
 * Plots data on Graph
 */

public class DataPlotter extends DataVisualiser
{
	private int circleSize;

	public DataPlotter(Graph graph, DiscreteData<? extends Number, ? extends Number> data, int circleSize)
	{
		super(graph, data);
		this.circleSize = circleSize;
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(this.getColor());
		Plane plane = this.getGraph().getPlane();
		// Iterate through all data
		for (var e : this.getData().getEntrySet())
		{
			int xPos = plane.posX(e.getKey()) - this.circleSize / 2;
			int yPos = plane.posY(e.getValue()) - this.circleSize / 2;
			g.fillOval(xPos, yPos, this.circleSize, this.circleSize);
		}
	}
}