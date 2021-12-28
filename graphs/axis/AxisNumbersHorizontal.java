package graphs.axis;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import data.DataPrecision;
import graphs.Range;

/**
 * A horizontal instance of GraphAxisNumbers
**/

public class AxisNumbersHorizontal extends AxisNumbers
{
	public AxisNumbersHorizontal(AxisTickmarks axis, Range range, DataPrecision precision)
	{
		super(axis, range, precision);
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
		int firstRightX = metrics.stringWidth(this.getNumberString(0)) + this.paddingHorizontal;
		for (int i = 1; i < this.getNumTicks() - 1; i++)
		{
			int secondLeftX = this.getNumberLeftPos(metrics, i);
			if (firstRightX > secondLeftX)
			{
				return true;
			}
			firstRightX = this.getNumberRightPos(metrics, i) + this.paddingHorizontal;
		}
		return firstRightX > this.getWidth() - metrics.stringWidth(this.getNumberString(this.getNumTicks() - 1));
	}

	@Override
	public int getPrecision()
	{
		return super.precision.getX();
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
		g.drawString(this.getNumberString(0), 0, this.getNumberBottomPos(metrics, bounds));
		String lastString = this.getNumberString(this.getNumTicks() - 1);
		g.drawString(lastString, bounds.width - metrics.stringWidth(lastString), this.getNumberBottomPos(metrics, bounds));
		
		// Print numbers inbetween
		for (int i = 1; i < this.getNumTicks() - 1; i++)
		{
			g.drawString(this.getNumberString(i), this.getNumberLeftPos(metrics, i), this.getNumberBottomPos(metrics, bounds));
		}
	}
	
	private int getNumberBottomPos(FontMetrics metrics, Rectangle bounds)
	{
		return bounds.height - metrics.getDescent();
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
