package graphs;

import java.util.LinkedList;
import java.awt.Graphics;

/**
 * A collection of GraphComponents. Contains methods for drawing all components for Graph rendering.
**/

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