package graphs.visualiser;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import data.functions.DiscreteData;
import graphs.Graph;
import graphs.GraphComponent;

/**
 * Component that visualises data on Graph
**/

public abstract class DataVisualiser extends GraphComponent implements PropertyChangeListener
{
	private DiscreteData<? extends Number, ? extends Number> data;
	private Color color = Color.BLACK;

	public DataVisualiser(Graph graph, DiscreteData<? extends Number, ? extends Number> data)
	{
		super(graph);
		this.data = data;
		this.data.addPropertyChangeListener​(this);
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

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		if (evt.getSource() == this.data)
		{
			this.repaint();
		}
	}
}
