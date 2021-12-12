package graphs;

import java.util.LinkedList;
import javax.swing.JComponent;
import java.awt.Graphics;

// NOTE: Graph should be able to implement bar graphs as well
// Actually, the data viewer should be a whole other class, and the Graph class can handle it
// If possible, parts of Graph can be wrapped in interfaces and used for drawing diagrams too
/* What should a graph component do?
It should act as a component
It should also view data through a viewport
In fact, it is like a JTable
Therefore, JTable should be used as inspiration
The difference between a JTable and a Graph, other than amount of data allowed, necessity of headers, scaling, and units, is how data is displayed
Data can be displayed through a scatterplot, line of best fit, or (like in this project) a line graph
Therefore, since the Graph is an abstract class, it should allow for implementation of all of these as child classes
Therefore, the Graph class should have the abstract method "draw"
*/

/* Ideas for class
Tooltip for data points showing values
Restrictions on scrolling
*/

public class Graph extends JComponent
{
	private Plane plane;
	private GraphComponentCollection components;
	private LinkedList<DiscreteData<?, ?>> dataList;

	public Graph()
	{
		this.components = new GraphComponentCollection();
		this.dataList = new LinkedList<>();
	}

	public void setLinearPlane(Range rangeX, Range rangeY)
	{
		this.plane = new LinearPlane(this, rangeX, rangeY);
	}

	public Plane getPlane()
	{
		return this.plane;
	}

	public GraphComponentCollection getGraphComponents()
	{
		return this.components;
	}

	public LinkedList<DiscreteData<?, ?>> getDataList()
	{
		return this.dataList;
	}

	public <N extends Number, M extends Number> void showData(DiscreteData<N, M> data)
	{
		if (this.dataList.contains(data))
		{
			this.components.add(new DataPlotter<N, M>(this, data, 10));
		}
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		this.components.paintComponents(g);
	}
}