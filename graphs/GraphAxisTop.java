package graphs;

import java.awt.Dimension;
import java.awt.Graphics;

public class GraphAxisTop extends GraphAxis
{
	public GraphAxisTop(Graph graph)
	{
		super(graph);
	}

	@Override
	public void modifyPreferredSize(Dimension size)
	{
		size.height = this.getTickLength() + 1;
	}

	@Override
	protected void drawLine(Graphics g)
	{
		g.drawLine(0, this.getTickLength(), this.getGraph().getWidth(), this.getTickLength());
	}

	@Override
	protected void drawTicks(Graphics g)
	{
		int cols = this.getGraph().getGrid().getNumCols();
		double tickOffset = (double) (this.getGraph().getGrid().getWidth() - 1) / cols;
		for (int i = 0; i <= cols; i++)
		{
			g.drawLine((int) (i * tickOffset), 0, (int) (i * tickOffset), this.getTickLength());
		}
	}
}
