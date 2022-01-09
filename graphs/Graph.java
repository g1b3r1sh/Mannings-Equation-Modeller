package graphs;

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

/**
 * Represents a graph. Handling of responsibilities such as adding data and displaying it is delegated to contained objects, which is an example of encapsulation.
**/

public class Graph extends JComponent
{
	private Plane plane;
	private Grid grid;
	private GraphComponentCollection components;
	private GraphDataCollection dataList;

	public Graph()
	{
		this.grid = new Grid(this, 1, 1);
		this.components = new GraphComponentCollection();
		this.dataList = new GraphDataCollection(this);
	}

	public void setLinearPlane(Range rangeX, Range rangeY)
	{
		this.plane = new LinearPlane(this, rangeX, rangeY);
	}

	public Plane getPlane()
	{
		return this.plane;
	}
	
	public void setGrid(int numCols, int numRows)
	{
		this.grid.setNumCols(numCols);
		this.grid.setNumRows(numRows);
	}
	
	// Fit gridpane for scales
	public boolean fitGridPlane(double scaleX, double scaleY)
	{
		try
		{
			this.grid.setNumCols((int) (this.plane.getRangeX().size() / scaleX));
			this.grid.setNumRows((int) (this.plane.getRangeY().size() / scaleY));
			return true;
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public Grid getGrid()
	{
		return this.grid;
	}

	public GraphComponentCollection getGraphComponents()
	{
		return this.components;
	}

	public GraphDataCollection getDataList()
	{
		return this.dataList;
	}

	// Requires both graphs to have set up Plane
	// Does not copy type of plane, only the ranges in it
	// Does not copy datalist nor graphcomponents
	public void lightCopy(Graph graph)
	{
		if (this.getPlane() != null && graph.getPlane() != null)
		{
			Plane plane = graph.getPlane();
			this.getPlane().getRangeX().copy(plane.getRangeX());
			this.getPlane().getRangeY().copy(plane.getRangeY());
		}
		this.getGrid().setNumCols(graph.getGrid().getNumCols());
		this.getGrid().setNumRows(graph.getGrid().getNumRows());
	}
	
	// Get dimensions on which pixels are visible, since pixels on edge are not visible
	public int getCanvasWidth()
	{
		return this.getWidth() - 1;
	}

	public int getCanvasHeight()
	{
		return this.getHeight() - 1;
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		if (this.grid != null)
		{
			this.grid.paintComponent(g);
		}
		this.components.paintComponents(g);
	}
}