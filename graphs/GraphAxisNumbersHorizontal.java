package graphs;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

public class GraphAxisNumbersHorizontal extends GraphAxisNumbers
{
	public GraphAxisNumbersHorizontal(GraphAxis axis, Range range) {
		super(axis, range);
	}

	@Override
	public int getTickPos(int i)
	{
		return (int) (this.getWidth() * this.getGraphAxis().calcTickFraction(i));
	}
	
	@Override
	public boolean isOverlapping()
	{
		FontMetrics metrics = this.getGraphics().getFontMetrics(this.getFont());
		int firstRightX = metrics.stringWidth(this.getNumberString(0)) + this.padding;
		for (int i = 1; i < this.getNumTicks() - 1; i++)
		{
			int secondLeftX = this.getNumberLeftPos(metrics, i);
			if (firstRightX > secondLeftX)
			{
				return true;
			}
			firstRightX = this.getNumberRightPos(metrics, i) + padding;
		}
		return firstRightX > this.getWidth() - metrics.stringWidth(this.getNumberString(this.getNumTicks() - 1));
	}

	@Override
	protected void modifyPreferredSize(Dimension size)
	{
		size.height = this.getGraphics().getFontMetrics(this.getFont()).getHeight();
	}

	@Override
	protected void paintNumbers(Graphics g, Rectangle bounds)
	{
		FontMetrics metrics = g.getFontMetrics();
		// Special case for numbers on sides
		g.drawString(this.getNumberString(0), 0, bounds.height - metrics.getDescent());
		g.drawString(this.getNumberString(this.getRange().size()), 
			bounds.width - metrics.stringWidth(this.getNumberString(this.getRange().size())), 
			bounds.height - metrics.getDescent()
		);
		// Print numbers inbetween
		for (int i = 1; i < this.getNumTicks() - 1; i++)
		{
			g.drawString(this.getNumberString(i), this.getNumberLeftPos(metrics, i), bounds.height - metrics.getDescent());
		}
	}

	private int getNumberLeftPos(FontMetrics metrics, int i)
	{
		return this.getTickPos(i) - (metrics.stringWidth(this.getNumberString(i)) / 2);
	}
	
	private int getNumberRightPos(FontMetrics metrics, int i)
	{
		return this.getTickPos(i) + (metrics.stringWidth(this.getNumberString(i)) / 2);
	}
}
