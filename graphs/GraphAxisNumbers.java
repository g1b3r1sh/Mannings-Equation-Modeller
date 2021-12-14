package graphs;

import javax.swing.JComponent;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

// TODO: Write getter and setter functions for protected variables
public abstract class GraphAxisNumbers extends JComponent
{
	private Graph graph;
	private Range range;
	protected int fontSize = 10;
	protected int precision = 2; // How many digits after decimal point
	protected int sidePadding = 3;
	protected int padding = 5;

	public GraphAxisNumbers(Graph graph, Range range)
	{
		this.graph = graph;
		this.range = range;
	}

	public Graph getGraph()
	{
		return graph;
	}
	
	// TODO: Restrict to precision
	public String getNumber(int i)
	{
		return String.format("%d", this.range.getNumber(i / (this.numTicks() - 1)));
	}
	
	public abstract int numTicks();

	public abstract boolean isOverlapping();

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
		g.setColor(Color.BLACK);
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		g.drawString("-3.14159262", 0, 20);
		//g.fillRect(0, 0, 2000, 2000);
	}
	
	protected abstract void paintNumbers();
}
