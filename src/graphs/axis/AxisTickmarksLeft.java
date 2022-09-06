package graphs.axis;

import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Represents a GraphAxis to the left of a graph
**/

public class AxisTickmarksLeft extends AxisTickmarks
{
	public AxisTickmarksLeft(int numTicks)
	{
		super(numTicks);
	}

	@Override
	public void modifyPreferredSize(Dimension size)
	{
		size.width = this.getTickLength() + 1;
	}

	@Override
	protected void drawLine(Graphics g)
	{
		g.drawLine(this.getTickLength(), 0, this.getTickLength(), this.getHeight());
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
