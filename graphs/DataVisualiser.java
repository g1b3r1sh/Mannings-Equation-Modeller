package graphs;

import java.awt.Color;

/**
 * Component that visualises data on Graph
 */

public abstract class DataVisualiser extends GraphComponent
{
	private DiscreteData<? extends Number, ? extends Number> data;
	private Color color = Color.BLACK;

	public DataVisualiser(Graph graph, DiscreteData<? extends Number, ? extends Number> data)
	{
		super(graph);

		this.data = data;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}

	public Color getColor()
	{
		return this.color;
	}

	public DiscreteData<? extends Number, ? extends Number> getData()
	{
		return this.data;
	}
}
