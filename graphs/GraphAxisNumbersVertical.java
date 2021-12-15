package graphs;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

public class GraphAxisNumbersVertical extends GraphAxisNumbers
{
	public GraphAxisNumbersVertical(GraphAxis axis, Range range) {
		super(axis, range);
	}

	@Override
	public int getTickPos(int i)
	{
		return (int) invertY(this.getHeight() * this.getGraphAxis().calcTickFraction(i), this.getHeight());
	}
	
	// NOTE: Not done
	@Override
	public boolean isOverlapping()
	{
		FontMetrics metrics = this.getGraphics().getFontMetrics(this.getFont());
		int firstTopX = metrics.getHeight() + this.padding;
		for (int i = 1; i < this.getNumTicks() - 1; i++)
		{
			int secondBottomX = this.getNumberLeftPos(metrics, i);
			if (firstTopX > secondBottomX)
			{
				return true;
			}
			firstTopX = this.getNumberRightPos(metrics, i) + padding;
		}
		return firstTopX > this.getWidth() - metrics.stringWidth(this.getNumberString(this.getNumTicks() - 1));
	}

	@Override
	protected void modifyPreferredSize(Dimension size)
	{
		FontMetrics metrics = this.getGraphics().getFontMetrics(this.getFont());
		int maxWidth = metrics.stringWidth(this.getNumberString(0));
		for (int i = 1; i < this.getNumTicks(); i++)
		{
			if (maxWidth < metrics.stringWidth(this.getNumberString(i)))
			{
				maxWidth = metrics.stringWidth(this.getNumberString(i));
			}
		}
		size.height = maxWidth;
	}

	// NOTE: Not done
	// TODO: Figure out how to align numbers, since left is different from right
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
			//g.drawString(this.getNumberString(i), );
		}
	}

	private double invertY(double pos, double max)
	{
		return max - pos;
	}
}
