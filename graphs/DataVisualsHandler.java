package graphs;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Predicate;

/**
 * Responsible for handling how an instance of DiscreteData is drawn on a graph
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

	public DataLineConnector connectData()
	{
		return this.addVisualiser(new DataLineConnector(this.graph, this.data));
	}

	public void unconnectData()
	{
		this.removeVisualisers(visualiser -> visualiser instanceof DataLineConnector);
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

	private <V extends DataVisualiser> V addVisualiser(V visualiser)
	{
		this.graph.getGraphComponents().add(visualiser);
		this.dataVisuals.add(visualiser);
		return visualiser;
	}

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
