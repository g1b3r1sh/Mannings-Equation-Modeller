package graphs.axis;

import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Represents a GraphAxis at the top of a graph
**/

public class AxisTickmarksTop extends AxisTickmarks
{
	public AxisTickmarksTop(Axis axis, int numTicks)
	{
		super(axis, numTicks);
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
