package graphs;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Predicate;

import data.functions.DiscreteData;
import graphs.visualiser.ConnectedDataVisualiser;
import graphs.visualiser.DataPlotter;
import graphs.visualiser.DataVisualiser;

/**
 * Stores DiscreteData
 * Handles drawing data on a graph through DataVisualisers
**/
public class DataVisualsHandler
{
	private Graph graph;
	private DiscreteData<? extends Number, ? extends Number> data;
	private LinkedList<DataVisualiser> visualisers = new LinkedList<>();

	public DataVisualsHandler(Graph graph, DiscreteData<? extends Number, ? extends Number> data)
	{
		this.graph = graph;
		this.data = data;
	}

	public DiscreteData<? extends Number, ? extends Number> getData()
	{
		return this.data;
	}

	public DataPlotter plotData()
	{
		return this.addVisualiser(new DataPlotter(this.graph, this.data, 10));
	}
	
	public void unplotData()
	{
		this.removeVisualisers(visualiser -> visualiser instanceof DataPlotter);
	}

	public ConnectedDataVisualiser connectData()
	{
		return this.addVisualiser(new ConnectedDataVisualiser(this.graph, this.data));
	}

	public void unconnectData()
	{
		this.removeVisualisers(visualiser -> visualiser instanceof ConnectedDataVisualiser);
	}
	
	// Remove all visual components of data from graph
	public void clearData()
	{
		this.removeVisualisers(visualiser -> true);
	}

	private <V extends DataVisualiser> V addVisualiser(V visualiser)
	{
		this.graph.getGraphComponents().add(visualiser);
		this.visualisers.add(visualiser);
		return visualiser;
	}
	
	// Remove all visual components of data based on predicate (i.e. boolean function)
	private void removeVisualisers(Predicate<DataVisualiser> predicate)
	{
		Iterator<DataVisualiser> it = this.visualisers.iterator();
		while (it.hasNext())
		{
			DataVisualiser visualiser = it.next();
			if (predicate.test(visualiser))
			{
				this.disconnectVisualiser(visualiser);
				it.remove();
			}
		}
	}

	private void disconnectVisualiser(DataVisualiser visualiser)
	{
		this.graph.getGraphComponents().remove(visualiser);
		this.data.removePropertyChangeListener​(visualiser);
	}
}
