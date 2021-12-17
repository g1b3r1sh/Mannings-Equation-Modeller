package graphs;

import java.awt.Graphics;

/**
 * A class wherein its instances are contained inside a Graph
**/

public abstract class GraphComponent
{
	private Graph graph;

	public GraphComponent(Graph graph)
	{
		this.graph = graph;
	}

	public Graph getGraph()
	{
		return this.graph;
	}

	public int getWidth()
	{
		return this.graph.getCanvasWidth();
	}

	public int getHeight()
	{
		return this.graph.getCanvasHeight();
	}

	protected abstract void paintComponent(Graphics g);
}