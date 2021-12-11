package graphs;

import java.util.LinkedList;
import java.awt.Graphics;

public class GraphComponentCollection extends LinkedList<GraphComponent>
{
	public void paintComponents(Graphics g)
	{
		for (GraphComponent c : this)
		{
			c.paintComponent(g);
		}
	}
}