package graphs;

import java.awt.Graphics;

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
		return this.graph.getWidth();
	}

	public int getHeight()
	{
		return this.graph.getHeight();
	}

	protected abstract void paintComponent(Graphics g);
}