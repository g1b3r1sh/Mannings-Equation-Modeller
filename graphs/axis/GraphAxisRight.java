package graphs.axis;

import java.awt.Dimension;
import java.awt.Graphics;

import graphs.Graph;

/**
 * Represents a GraphAxis to the right of a graph
**/

public class GraphAxisRight extends GraphAxis
{
	public GraphAxisRight(Graph graph)
	{
		super(graph);
	}

	@Override
	public int getNumTicks()
	{
		return this.getGraph().getGrid().getNumRows() + 1;
	}

	@Override
	public void modifyPreferredSize(Dimension size)
	{
		size.width = this.getTickLength() + 1;
	}

	@Override
	protected void drawLine(Graphics g)
	{
		g.drawLine(0, 0, 0, this.getGraph().getHeight());
	}

	@Override
	protected void drawTicks(Graphics g)
	{
		double tickOffset = (double) (this.getHeight() - 1) / (this.getNumTicks() - 1);
		for (int i = 0; i < this.getNumTicks(); i++)
		{
			g.drawLine(0, (int) (i * tickOffset), this.getTickLength(), (int) (i * tickOffset));
		}
	}
}
