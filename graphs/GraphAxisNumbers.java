package graphs;

import javax.swing.JComponent;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class GraphAxisNumbers extends JComponent
{
	private Graph graph;
	boolean horizontal;

	public GraphAxisNumbers(Graph graph, GraphContainer.Direction direction)
	{
		this.graph = graph;
		this.horizontal = (direction == GraphContainer.Direction.TOP || direction == GraphContainer.Direction.BOTTOM);
	}

	@Override
	public Dimension getPreferredSize()
	{		
		Dimension size = super.getPreferredSize();
		if (this.horizontal)
		{
			size.height = 20;
		}
		else
		{
			
		}
		return size;
	}

	@Override
	public Dimension getMinimumSize()
	{
		// You have no options either
		return this.getPreferredSize();
	}

	// TODO: Scale font
	@Override
	protected void paintComponent(Graphics g)
	{
		if (this.horizontal)
		{
			
		}
		else
		{

		}
		g.setColor(Color.BLACK);
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		g.drawString("-3.14159262", 0, 20);
		//g.fillRect(0, 0, 2000, 2000);
	}
}
