package graphs;

import java.awt.Graphics;
import java.util.Iterator;
import java.util.Set;

import data.DiscreteData;

/**
 * Paints connection between data points on graph
**/

public class DataLineConnector extends DataVisualiser
{
	public DataLineConnector(Graph graph, DiscreteData<?, ?> data)
	{
		super(graph, data);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		Set<? extends Number> xSet = this.getData().getXSet();
		// Can't paint connections if there is less than two points
		if (xSet.size() > 1)
		{
			g.setColor(this.getColor());
			Plane plane = this.getGraph().getPlane();
			// Iterate through points in twos
			Iterator<? extends Number> itFirst = xSet.iterator();
			Iterator<? extends Number> itSecond = xSet.iterator();
			itSecond.next();
			while (itSecond.hasNext())
			{
				Number n1 = itFirst.next();
				Number n2 = itSecond.next();
				g.drawLine(
					plane.posX(n1), 
					plane.posY(this.getData().yDouble(n1)), 
					plane.posX(n2), 
					plane.posY(this.getData().yDouble(n2))
				);
			}
		}
	}
}
