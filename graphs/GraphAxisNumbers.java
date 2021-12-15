package graphs;

import javax.swing.JComponent;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

// TODO: Write getter and setter functions for protected variables
public abstract class GraphAxisNumbers extends JComponent
{
	private Graph graph;
	private Range range;
	private final int MAX_FONT = 20;
	protected Font font;
	protected int precision = 2; // How many digits after decimal point
	protected int sidePadding = 3;
	protected int padding = 5;

	public GraphAxisNumbers(Graph graph, Range range)
	{
		this.graph = graph;
		this.range = range;
		this.font = new Font(Font.SANS_SERIF, Font.PLAIN, this.MAX_FONT);
	}

	public Graph getGraph()
	{
		return this.graph;
	}
	
	public Range getRange()
	{
		return this.range;
	}
	
	// TODO: Restrict to precision
	// Get number for ith tick
	public String getNumberString(int i)
	{
		return String.format("%." + this.precision + "f", this.range.getNumber((double) i / (this.numTicks() - 1)));
	}
	
	public int numTicks()
	{
		return this.range.size() + 1;
	}

	public abstract boolean isOverlapping();
	
	public void scaleFont()
	{
		this.font = this.font.deriveFont(this.MAX_FONT);
		while (this.isOverlapping())
		{
			this.font = this.font.deriveFont(this.font.getSize() - 1);
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

	// TODO: Scale font
	@Override
	protected void paintComponent(Graphics g)
	{
		//g.setColor(Color.RED);
		//g.fillRect(0, 0, 2000, 2000);
		g.setColor(Color.BLACK);
		g.setFont(this.font);
		this.paintNumbers(g, this.getBounds());
	}
	
	protected abstract void paintNumbers(Graphics g, Rectangle bounds);
}
