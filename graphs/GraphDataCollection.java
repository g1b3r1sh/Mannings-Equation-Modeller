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
		for (DataVisualsHandler wrapper : this.dataList)
		{
			if (wrapper.getData() == data)
			{
				return true;
			}
		}
		return false;
	}

	public DataVisualsHandler getWrapper(DiscreteData<?, ?> data)
	{
		for (DataVisualsHandler wrapper : this.dataList)
		{
			if (wrapper.getData() == data)
			{
				return wrapper;
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
		DataVisualsHandler wrapper = this.getWrapper(data);
		if (wrapper != null)
		{
			wrapper.clearData();
			this.dataList.remove(wrapper);
		}
	}
}
