package graphs.axis;

import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Represents a GraphAxis to the left of a graph
**/

public class AxisTickmarksLeft extends AxisTickmarks
{
	public AxisTickmarksLeft(Axis axis)
	{
		super(axis);
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
		g.drawLine(this.getTickLength(), 0, this.getTickLength(), this.getGraph().getHeight());
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
