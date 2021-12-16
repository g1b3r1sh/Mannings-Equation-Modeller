package graphs;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

public class GraphAxisNumbersVertical extends GraphAxisNumbers
{
	boolean alignRight;

	public GraphAxisNumbersVertical(GraphAxis axis, Range range, int precision, boolean alignRight) {
		super(axis, range, precision);
		this.alignRight = alignRight;
	}

	@Override
	public int getTickPos(int i)
	{
		return (int) invertY(this.getHeight() * this.getGraphAxis().calcTickFraction(i), this.getHeight());
	}
	
	@Override
	public boolean isOverlapping()
	{
		FontMetrics metrics = this.getGraphics().getFontMetrics(this.getFont());
		// Vertical axis goes from bottom to top
		int firstTopX = this.getHeight() - this.getNumberHeight(metrics) - this.paddingVertical;
		for (int i = 1; i < this.getNumTicks() - 1; i++)
		{
			int secondBottomX = this.getNumberBottomPos(metrics, i);
			if (firstTopX < secondBottomX)
			{
				return true;
			}
			firstTopX = this.getNumberTopPos(metrics, i) - paddingVertical;
		}
		return firstTopX < this.getNumberHeight(metrics);
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
		size.width = maxWidth;
	}
	
	@Override
	protected void paintNumbers(Graphics g, Rectangle bounds)
	{
		FontMetrics metrics = g.getFontMetrics();
		
		// Special case for numbers on sides
		g.drawString(this.getNumberString(0), this.getNumberLeftPos(metrics, bounds, 0), bounds.height);
		g.drawString(this.getNumberString(this.getNumTicks() - 1), 
			this.getNumberLeftPos(metrics, bounds, this.getNumTicks() - 1), 
			this.getNumberHeight(metrics)
		);
		
		// Print numbers inbetween
		for (int i = 1; i < this.getNumTicks() - 1; i++)
		{
			g.drawString(this.getNumberString(i), this.getNumberLeftPos(metrics, bounds, i), this.getNumberBottomPos(metrics, i));
		}
	}

	private int getNumberHeight(FontMetrics metrics)
	{
		return metrics.getHeight() - metrics.getDescent() - metrics.getDescent();
	}

	private int getNumberLeftPos(FontMetrics metrics, Rectangle bounds, int i)
	{
		if (this.alignRight)
		{
			return bounds.width - metrics.stringWidth(this.getNumberString(i));
		}
		else
		{
			return 0;
		}
	}

	private int getNumberTopPos(FontMetrics metrics, int i)
	{
		return this.getTickPos(i) - (this.getNumberHeight(metrics) / 2);
	}

	private int getNumberBottomPos(FontMetrics metrics, int i)
	{
		return this.getTickPos(i) + (this.getNumberHeight(metrics) / 2);
	}
	
	private double invertY(double pos, double max)
	{
		return max - pos;
	}
}
