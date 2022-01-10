package graphs.visualiser;

import java.awt.Graphics;

import data.DiscreteData;
import graphs.Graph;
import graphs.Plane;

/**
 * Plots DiscreteData on Graph
**/

public class DataPlotter extends DataVisualiser
{
	private int circleSize = 10;

	public DataPlotter(Graph graph, DiscreteData<? extends Number, ? extends Number> data)
	{
		super(graph, data);
	}

	public DataPlotter(Graph graph, DiscreteData<? extends Number, ? extends Number> data, int circleSize)
	{
		this(graph, data);
		this.setCircleSize(circleSize);
	}

	public void setCircleSize(int circleSize)
	{
		this.circleSize = circleSize;
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(this.getColor());
		Plane plane = this.getGraph().getPlane();
		for (var e : this.getData().getEntrySet())
		{
			this.paintPlotSymbol(g, plane.posX(e.getKey()), plane.posY(e.getValue()));
		}
	}

	protected void paintPlotSymbol(Graphics g, int centerX, int centerY)
	{
		g.fillOval(centerX - this.circleSize / 2, centerY - this.circleSize / 2, this.circleSize, this.circleSize);
	}
}