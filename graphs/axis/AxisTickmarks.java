package graphs.axis;

import javax.swing.JComponent;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;

/**
 * Represents the tickmarks that surround a graph.
**/

public abstract class AxisTickmarks extends JComponent
{
	private int numTicks;
	
	private int tickLength = 10;
	private double scale = 1;
	private Color color;

	public AxisTickmarks(int numTicks)
	{
		this.numTicks = numTicks;
	}

	public void getNumTicks(int numTicks)
	{
		this.numTicks = numTicks;
	}

	public int getNumTicks()
	{
		return this.numTicks;
	}

	public double getScale()
	{
		return scale;
	}

	public void setScale(double scale)
	{
		this.scale = scale;
	}

	public void setTickLength(int tickLength)
	{
		this.tickLength = tickLength;
	}

	public double calcTickFraction(int tick)
	{
		return ((double) tick) / (this.getNumTicks() - 1);
	}

	public int getTickLength()
	{
		return this.tickLength;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}

	public Color getColor()
	{
		return this.color;
	}
	
	@Override
	public Dimension getPreferredSize()
	{		
		Dimension size = super.getPreferredSize();
		this.modifyPreferredSize(size);
		return size;
	}

	protected abstract void modifyPreferredSize(Dimension size);

	@Override
	public Dimension getMinimumSize()
	{
		// You have no choice
		return this.getPreferredSize();
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(this.color);
		
		this.drawLine(g);
		this.drawTicks(g);
	}

	protected abstract void drawLine(Graphics g);
	protected abstract void drawTicks(Graphics g);
}
