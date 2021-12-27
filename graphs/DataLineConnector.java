package graphs;

import java.awt.Graphics;
import java.util.Iterator;
import java.util.Set;

import data.ContinuousData;
import data.DiscreteData;

/**
 * Paints connection between data points on graph
**/

public class DataLineConnector extends DataVisualiser
{
	ContinuousData<? extends Number, ? extends Number> connectedData;

	public <M extends Number, N extends Number> DataLineConnector(Graph graph, DiscreteData<M, N> data)
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
			Set<? extends Number> xSet = this.getData().getXSet();
			Iterator<? extends Number> itFirst = xSet.iterator();
			Iterator<? extends Number> itSecond = xSet.iterator();
			itSecond.next();
			while (itSecond.hasNext())
			{
				Number n1 = itFirst.next();
				Number n2 = itSecond.next();
				g.drawLine(
					plane.posX(n1), 
					plane.posY(this.connectedData.y(n1.doubleValue())), 
					plane.posX(n2), 
					plane.posY(this.connectedData.y(n2.doubleValue()))
				);
			}
		}
	}
}
