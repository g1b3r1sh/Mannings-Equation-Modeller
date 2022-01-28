package graphs.axis;

import javax.swing.JComponent;

import data.Range;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;

/**
 * Represents the numbers displayed in intervals along the axis of a graph.
**/

public abstract class AxisNumbers extends JComponent
{
	private final float RESIZE_INCREMENT = 0.1f;

	private Range fontRange;
	private int padding;

	private AxisTickmarks tickmarks;
	private Range range;
	private int scale;

	private int prevLength;

	// Make fontRange parameter
	public AxisNumbers(AxisTickmarks tickmarks, Range range, int scale, int padding)
	{
		this.fontRange = new Range(10, 20);

		this.tickmarks = tickmarks;
		this.range = range;
		this.scale = scale;
		this.padding = padding;
		this.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, this.fontRange.getUpper()));

		this.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				AxisNumbers.this.resizeFitFont();
			}
		});

		this.prevLength = this.getChangingLength();
	}

	public AxisTickmarks getTickmarks()
	{
		return this.tickmarks;
	}
	
	public Range getRange()
	{
		return this.range;
	}

	public int getScale()
	{
		return this.scale;
	}

	public void setScale(int scale)
	{
		this.scale = scale;
		this.repaint();
	}

	public Range getFontRange()
	{
		return this.fontRange;
	}

	public int getPadding()
	{
		return this.padding;
	}
	
	// Get number for (i + 1)th tick
	public double getNumber(int i)
	{
		return this.range.getNumber((double) i / (this.tickmarks.getNumTicks() - 1));
	}

	// Get number for ith tick
	public String getNumberString(int i)
	{
		return String.format("%." + this.scale + "f", this.getNumber(i));
	}

	public int getNumTicks()
	{
		return this.getTickmarks().getNumTicks();
	}

	public abstract int getTickPos(int i);

	public abstract boolean isOverlapping();

	// Algorithm is less efficient than resizeFitFont, but always works
	public void fitFont()
	{
		if (this.getGraphics() == null)
		{
			return;
		}

		float size = this.fontRange.getUpper();
		this.setFont(this.getFont().deriveFont(size));
		while (size > this.fontRange.getLower() && this.isOverlapping())
		{
			size -= this.RESIZE_INCREMENT;
			this.setFont(this.getFont().deriveFont(size));
		}
	}
	
	// Fits font after resizing along the changing length
	// Does not work if the length along which the font size should change didn't change
	public void resizeFitFont()
	{
		if (this.getGraphics() == null)
		{
			return;
		}
		
		int currLength = this.getChangingLength();
		float size = this.getFont().getSize2D();
		// Size has shrunk
		if (currLength < this.prevLength)
		{
			while (size > this.fontRange.getLower() && this.isOverlapping())
			{
				size -= this.RESIZE_INCREMENT;
				this.setFont(this.getFont().deriveFont(size));
			}
		}
		// Size has grown
		else if (currLength > this.prevLength)
		{
			while (size <= this.fontRange.getUpper() && !this.isOverlapping())
			{
				size += this.RESIZE_INCREMENT;
				this.setFont(this.getFont().deriveFont(size));
			}
		}
		this.prevLength = currLength;
	}

	@Override
	public Dimension getPreferredSize()
	{
		Dimension size = super.getPreferredSize();
		this.modifyPreferredSize(size);
		return size;
	}

	@Override
	public Dimension getMinimumSize()
	{
		// You have no options either
		return this.getPreferredSize();
	}

	protected abstract void modifyPreferredSize(Dimension size);

	protected abstract int getChangingLength();
	protected abstract int getConstantLength();

	@Override
	protected void paintComponent(Graphics g)
	{
		//g.setColor(Color.RED);
		//g.fillRect(0, 0, 2000, 2000);
		g.setColor(Color.BLACK);
		g.setFont(this.getFont());
		this.paintNumbers(g, this.getBounds());
	}
	
	protected abstract void paintNumbers(Graphics g, Rectangle bounds);
}
