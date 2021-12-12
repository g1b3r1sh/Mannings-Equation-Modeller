package graphs;

import java.util.LinkedList;

public class GraphDataCollection
{
	private Graph graph; // Graph to contain data in
	private LinkedList<DataVisualsHandler> dataList;

	public GraphDataCollection(Graph graph)
	{
		this.graph = graph;
		this.dataList = new LinkedList<>();
	}

	public boolean containsData(DiscreteData<?, ?> data)
	{
		for (DataVisualsHandler handler : this.dataList)
		{
			if (handler.getData() == data)
			{
				return true;
			}
		}
		return false;
	}

	public DataVisualsHandler getVisualsHandler(DiscreteData<?, ?> data)
	{
		for (DataVisualsHandler handler : this.dataList)
		{
			if (handler.getData() == data)
			{
				return handler;
			}
		}
		return null;
	}

	public void addData(DiscreteData<?, ?> data)
	{
		if (!this.containsData(data))
		{
			this.dataList.add(new DataVisualsHandler(this.graph, data));
		}
	}

	public void removeData(DiscreteData<?, ?> data)
	{
		DataVisualsHandler handler = this.getVisualsHandler(data);
		if (handler != null)
		{
			handler.clearData();
			this.dataList.remove(handler);
		}
	}
}
