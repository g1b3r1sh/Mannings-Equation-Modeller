package graphs;

import java.util.Iterator;
import java.util.LinkedList;

// TODO: Have a better name
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
		DataPlotter plotter = new DataPlotter(this.graph, this.data, 10);
		this.graph.getGraphComponents().add(plotter);
		this.dataVisuals.add(plotter);
	}
	
	public void unplotData()
	{
		Iterator<DataVisualiser> it = this.dataVisuals.iterator();
		while (it.hasNext())
		{
			DataVisualiser visualiser = it.next();
			if (visualiser instanceof DataPlotter)
			{
				this.graph.getGraphComponents().remove(visualiser);
				it.remove();
			}
		}
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
}
