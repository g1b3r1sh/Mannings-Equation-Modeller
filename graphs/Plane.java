package graphs;

public abstract class Plane extends GraphComponent 
{
	public Plane(Graph graph)
	{
		super(graph);
	}

	public abstract int posX(double x);
	public abstract int posY(double y);
}