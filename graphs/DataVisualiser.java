package graphs;

public abstract class DataVisualiser extends GraphComponent
{
	private DiscreteData<? extends Number, ? extends Number> data;

	public DataVisualiser(Graph graph, DiscreteData<? extends Number, ? extends Number> data)
	{
		super(graph);

		this.data = data;
	}

	public DiscreteData<? extends Number, ? extends Number> getData()
	{
		return this.data;
	}
}
