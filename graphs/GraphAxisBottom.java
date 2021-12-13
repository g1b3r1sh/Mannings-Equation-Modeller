package graphs;

import java.awt.Dimension;
import java.awt.Graphics;

public class GraphAxisBottom extends GraphAxis
{
	public GraphAxisBottom(Graph graph)
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
		g.drawLine(0, 0, this.getGraph().getWidth(), 0);
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
