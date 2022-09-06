package graphs;

import java.awt.Graphics;

/**
 * Represents a component that is contained inside a graph
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

	public void repaint()
	{
		this.graph.repaint();
	}

	protected abstract void paintComponent(Graphics g);
}