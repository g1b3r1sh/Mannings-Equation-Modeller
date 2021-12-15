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
	public int getNumTicks()
	{
		return this.getGraph().getGrid().getNumCols() + 1;
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
		double tickOffset = (double) (this.getWidth() - 1) / (this.getNumTicks() - 1);
		for (int i = 0; i < this.getNumTicks(); i++)
		{
			g.drawLine((int) (i * tickOffset), 0, (int) (i * tickOffset), this.getTickLength());
		}
	}
}
