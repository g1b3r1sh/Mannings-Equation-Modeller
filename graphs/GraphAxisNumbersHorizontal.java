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
	
	// TODO: Fix overlapping class
	@Override
	public boolean isOverlapping()
	{
		/*FontMetrics metrics = this.getGraphics().getFontMetrics(this.getFont());
		int sum = (this.getNumTicks() - 1) * this.padding;
		for (int i = 0; i < this.getNumTicks(); i++)
		{
			sum += metrics.stringWidth(this.getNumberString(i));
		}
		return sum > this.getWidth();*/
		return false;
	}

	@Override
	protected void modifyPreferredSize(Dimension size)
	{
		this.scaleFont();
		size.height = this.getGraphics().getFontMetrics(this.getFont()).getHeight();
	}

	@Override
	protected void paintNumbers(Graphics g, Rectangle bounds)
	{
		FontMetrics metrics = g.getFontMetrics();
		// Special case for numbers on sides
		g.drawString(this.getNumberString(0), 0, bounds.height);
		g.setFont(this.getFont().deriveFont(19f));
		g.drawString(this.getNumberString(this.getRange().size()), bounds.width - metrics.stringWidth(this.getNumberString(1)), bounds.height);
		// Print numbers inbetween
		/*for (int i = 1; i < this.getNumTicks() - 1; i++)
		{
			int middleX = (int) (bounds.width * this.getGraphAxis().calcTickFraction(i));
		}*/
	}
}
