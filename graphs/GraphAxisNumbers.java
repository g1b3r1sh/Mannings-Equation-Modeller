package graphs;

import javax.swing.JComponent;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;

// TODO: Write getter and setter functions for protected variables
public abstract class GraphAxisNumbers extends JComponent implements ComponentListener
{
	private GraphAxis axis;
	private Range range;
	private final int MAX_FONT = 30;
	private final int MIN_FONT = 10;
	protected int precision = 2; // How many digits after decimal point
	protected int padding = 50;

	public GraphAxisNumbers(GraphAxis axis, Range range)
	{
		this.axis = axis;
		this.range = range;
		this.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, this.MAX_FONT));

		this.addComponentListener(this);
	}

	@Override
	public void componentResized(ComponentEvent e)
	{
		this.fitFont();
	}
	
	@Override
    public void componentHidden(ComponentEvent e) {}

	@Override
    public void componentMoved(ComponentEvent e) {}

	@Override
    public void componentShown(ComponentEvent e) {}

	public GraphAxis getGraphAxis()
	{
		return this.axis;
	}
	
	public Range getRange()
	{
		return this.range;
	}
	
	// Get number for (i + 1)th tick
	public double getNumber(int i)
	{
		return this.range.getNumber((double) i / (this.axis.getNumTicks() - 1));
	}

	// Get number for ith tick
	public String getNumberString(int i)
	{
		return String.format("%." + this.precision + "f", this.getNumber(i));
	}

	public int getNumTicks()
	{
		return this.getGraphAxis().getNumTicks();
	}

	public abstract int getTickPos(int i);

	public abstract boolean isOverlapping();
	
	public void fitFont()
	{
		this.setFont(this.getFont().deriveFont((float) this.MAX_FONT));
		float size = this.MAX_FONT;
		while (this.isOverlapping() && size > this.MIN_FONT)
		{
			size -= 0.1f;
			this.setFont(this.getFont().deriveFont(size));
			if (this.getFont().getSize() < 0)
			{
				break;
			}
		}
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
