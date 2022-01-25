package graphs;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GraphController implements PropertyChangeListener
{
	public GraphContainer container;

	public GraphController(GraphContainer container)
	{
		this.container = container;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		if ("update".equals(evt.getPropertyName()) && evt.getNewValue() instanceof GraphContainer)
		{
			GraphContainer newContainer = (GraphContainer) evt.getNewValue();
			// Set values of newContainer to this.container
			this.container.lightCopy(newContainer);
			this.container.repaint();
		}
	}
}
