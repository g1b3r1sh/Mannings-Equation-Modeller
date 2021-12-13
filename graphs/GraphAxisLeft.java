package graphs;

import java.awt.Dimension;
import java.awt.Graphics;

public class GraphAxisLeft extends GraphAxis
{
	public GraphAxisLeft(Graph graph)
	{
		super(graph);
	}

	@Override
	public void modifyPreferredSize(Dimension size)
	{
		size.width = this.getTickLength() + 1;
	}

	@Override
	protected void drawLine(Graphics g)
	{
		g.drawLine(this.getTickLength(), 0, this.getTickLength(), this.getGraph().getHeight());
	}

	@Override
	protected void drawTicks(Graphics g)
	{
		int rows = this.getGraph().getGrid().getNumRows();
		double tickOffset = (double) (this.getGraph().getGrid().getHeight() - 1) / rows;
		for (int i = 0; i <= rows; i++)
		{
			g.drawLine(0, (int) (i * tickOffset), this.getTickLength(), (int) (i * tickOffset));
		}
	}
}
