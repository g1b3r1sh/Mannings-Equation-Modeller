package graphs;

import java.awt.Graphics;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import data.ContinuousData;
import data.DiscreteData;

/**
 * Paints connection between data points on graph
**/

public class ConnectedDataVisualiser extends DataVisualiser
{
	ContinuousData<? extends Number, ? extends Number> connectedData;

	public <M extends Number, N extends Number> ConnectedDataVisualiser(Graph graph, DiscreteData<M, N> data)
	{
		super(graph, data);
		this.connectedData = new ContinuousData<M, N>(data);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		// Can't paint connections if there is less than two points
		if (this.getData().size() > 1)
		{
			g.setColor(this.getColor());
			Plane plane = this.getGraph().getPlane();
			// Iterate through points in twos
			java.util.Set<? extends Entry<? extends Number, ? extends Number>> entries = this.getData().getEntrySet();
			Iterator<? extends Entry<? extends Number, ? extends Number>> itFirst = entries.iterator();
			Iterator<? extends Entry<? extends Number, ? extends Number>> itSecond = entries.iterator();
			itSecond.next();
			while (itSecond.hasNext())
			{
				Entry<? extends Number, ? extends Number> n1 = itFirst.next();
				Entry<? extends Number, ? extends Number> n2 = itSecond.next();
				g.drawLine(
					plane.posX(n1.getKey()), 
					plane.posY(n1.getValue()), 
					plane.posX(n2.getKey()), 
					plane.posY(n2.getValue())
				);
			}
		}
	}
}
