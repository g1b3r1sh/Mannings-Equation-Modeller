package graphs;

import java.awt.Dimension;

import graphs.GraphContainer.Direction;

public class GraphAxisNumbersHorizontal extends GraphAxisNumbers
{
	public GraphAxisNumbersHorizontal(Graph graph, Range range) {
		super(graph, range);
	}

	@Override
	protected void modifyPreferredSize(Dimension size)
	{
		size.height = this.fontSize;
	}

	@Override
	protected void paintNumbers()
	{
		// Special case for numbers on side of 
	}

}
