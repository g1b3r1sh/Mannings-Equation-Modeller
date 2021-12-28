package graphs;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Predicate;

import data.DiscreteData;

/**
 * Stores DiscreteData and is responsible for handling how it is drawn on a graph
 * Handles drawing through DataVisualisers
**/
public class DataVisualsHandler
{
	private Graph graph;
	private DiscreteData<?, ?> data;
	private LinkedList<DataVisualiser> dataVisuals;

	public DataVisualsHandler(Graph graph, DiscreteData<?, ?> data)
	{
		this.graph = graph;
		this.data = data;

		this.dataVisuals = new LinkedList<>();
	}

	public DiscreteData<?, ?> getData()
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
		Iterator<DataVisualiser> it = this.dataVisuals.iterator();
		while (it.hasNext())
		{
			DataVisualiser visualiser = it.next();
			this.graph.getGraphComponents().remove(visualiser);
			it.remove();
		}
	}

	private <V extends DataVisualiser> V addVisualiser(V visualiser)
	{
		this.graph.getGraphComponents().add(visualiser);
		this.dataVisuals.add(visualiser);
		return visualiser;
	}
	
	// Remove all visual components of data based on condition
	// Can be used for removing all instances of specific DataVisualiser class by passing "visualiser -> visualiser instanceof class" as parameter
	private void removeVisualisers(Predicate<DataVisualiser> predicate)
	{
		Iterator<DataVisualiser> it = this.dataVisuals.iterator();
		while (it.hasNext())
		{
			DataVisualiser visualiser = it.next();
			if (predicate.test(visualiser))
			{
				this.graph.getGraphComponents().remove(visualiser);
				it.remove();
			}
		}
	}
}
