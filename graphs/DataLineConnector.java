package graphs;

import java.awt.Graphics;
import java.util.Iterator;
import java.util.Set;

public class DataLineConnector extends DataVisualiser
{
	public DataLineConnector(Graph graph, DiscreteData<?, ?> data)
	{
		super(graph, data);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(this.getColor());
		Plane plane = this.getGraph().getPlane();
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
				plane.posY(this.getData().yDouble(n1)), 
				plane.posX(n2), 
				plane.posY(this.getData().yDouble(n2))
			);
		}
	}
}
