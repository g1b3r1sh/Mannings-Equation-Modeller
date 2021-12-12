package graphs;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Predicate;

// TODO: Have a better name
// Responsible for handling interaction between data visualisers and graph
public class DataWrapper
{
	private Graph graph;
	private DiscreteData<?, ?> data;
	private LinkedList<DataVisualiser> dataVisuals;

	public DataWrapper(Graph graph, DiscreteData<?, ?> data)
	{
		this.graph = graph;
		this.data = data;

		this.dataVisuals = new LinkedList<>();
	}

	public DiscreteData<?, ?> getData()
	{
		return this.data;
	}

	public void plotData()
	{
		this.addVisualiser(new DataPlotter(this.graph, this.data, 10));
	}
	
	public void unplotData()
	{
		this.removeVisualiser(visualiser -> visualiser instanceof DataPlotter);
	}

	public void connectData()
	{
		this.addVisualiser(new DataLineConnector(this.graph, this.data));
	}

	public void unconnectData()
	{
		this.removeVisualiser(visualiser -> visualiser instanceof DataLineConnector);
	}

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

	private void addVisualiser(DataVisualiser visualiser)
	{
		this.graph.getGraphComponents().add(visualiser);
		this.dataVisuals.add(visualiser);
	}

	private void removeVisualiser(Predicate<DataVisualiser> predicate)
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
