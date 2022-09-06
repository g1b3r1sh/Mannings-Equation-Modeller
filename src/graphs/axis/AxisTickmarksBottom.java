package graphs.axis;

import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Represents a GraphAxis at the bottom of a graph
**/

public class AxisTickmarksBottom extends AxisTickmarks
{
	public AxisTickmarksBottom(int numTicks)
	{
		super(numTicks);
	}
	
	@Override
	public void modifyPreferredSize(Dimension size)
	{
		size.height = this.getTickLength() + 1;
	}

	@Override
	protected void drawLine(Graphics g)
	{
		g.drawLine(0, 0, this.getWidth(), 0);
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
