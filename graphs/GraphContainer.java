package graphs;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.util.function.Consumer;
import java.util.function.Function;
import java.awt.GridBagConstraints;
import java.awt.Component;

// Useful site: https://thebadprogrammer.com/swing-layout-manager-sizing/
// FUTURE: Enable scrollpane (scale axis to pane, zoom in/zoom out features)
public class GraphContainer extends JComponent
{
	public static enum Direction
	{
		TOP,
		LEFT,
		RIGHT,
		BOTTOM
	}

	Graph graph;

	JPanel containerPanel;
	JPanel topPanel;
	JPanel rightPanel;
	JPanel bottomPanel;
	JPanel leftPanel;

	JScrollPane scrollPane;

	public GraphContainer(Graph graph)
	{
		this.setLayout(new BorderLayout());
		
		this.graph = graph;
		this.containerPanel = createContainerPanel();
		//this.scrollPane = new JScrollPane(this.graph);
		//this.scrollPane.setPreferredSize(this.graph.getPreferredSize());
		
		GridBagConstraints graphConstraints = new GridBagConstraints();
		graphConstraints.gridx = 1;
		graphConstraints.gridy = 1;
		graphConstraints.fill = GridBagConstraints.BOTH;
		graphConstraints.weightx = 1;
		graphConstraints.weighty = 1;

		//this.containerPanel.add(this.scrollPane, graphConstraints);
		this.containerPanel.add(this.graph, graphConstraints);
		this.add(this.containerPanel);
	}

	public Graph getGraph()
	{
		return this.graph;
	}

	public boolean containsAxis(Direction direction)
	{
		for (Component c : this.getPanel(direction).getComponents())
		{
			if (c instanceof GraphAxis)
			{
				return true;
			}
		}
		return false;
	}

	public void addAxis(Direction direction)
	{
		if (!this.containsAxis(direction))
		{
			this.addComponent(direction, this.createAxis(direction), 0);
		}
	}

	// Returns null if axis doesn't exist
	public GraphAxis getAxis(Direction direction)
	{
		if (this.containsAxis(direction))
		{
			return (GraphAxis) this.getComponent(direction, 0);
		}
		return null;
	}

	public void removeAxis(Direction direction)
	{
		if (this.containsAxis(direction))
		{
			this.removeComponent(direction, 0);
			if (this.containsNumbers(direction))
			{
				this.removeComponent(direction, 1);
			}
		}
	}
	
	public boolean containsNumbers(Direction direction)
	{
		for (Component c : this.getPanel(direction).getComponents())
		{
			if (c instanceof GraphAxisNumbers)
			{
				return true;
			}
		}
		return false;
	}

	public void addNumbers(Direction direction)
	{
		if (this.containsAxis(direction) && !this.containsNumbers(direction))
		{
			if (this.horizontal(direction))
			{
				this.addComponent(direction, new GraphAxisNumbersHorizontal(this.getAxis(direction), this.graph.getPlane().getRangeX()), 1);
			}
			else
			{
				this.addComponent(direction, new GraphAxisNumbersVertical(this.getAxis(direction), this.graph.getPlane().getRangeY()), 1);
			}
		}
	}

	public void removeNumbers(Direction direction)
	{
		if (this.containsNumbers(direction))
		{
			this.removeComponent(direction, 1);
		}
	}

	public void addAxisName(Direction direction, String name)
	{
		JLabel label = new JLabel(name);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.setAlignmentY(Component.CENTER_ALIGNMENT);
		this.addComponent(direction, label);
	}

	public void removeAxisName(Direction direction)
	{
		if (!(this.getComponent(direction) instanceof GraphAxis))
		{
			this.removeComponent(direction);
		}
	}

	public boolean horizontal(Direction direction)
	{
		return direction == Direction.BOTTOM || direction == Direction.TOP;
	}

	public boolean forwardSequential(Direction direction)
	{
		return direction == Direction.BOTTOM || direction == Direction.RIGHT;
	}

	// Creates GridBag layout with four BoxLayout panels surrounding empty center
	private JPanel createContainerPanel()
	{
		containerPanel = new JPanel(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		this.bottomPanel = createSidePanel(true);
		containerPanel.add(this.bottomPanel, constraints);
		constraints.gridy = 0;
		this.topPanel = createSidePanel(true);
		containerPanel.add(this.topPanel, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		this.leftPanel = createSidePanel(false);
		constraints.fill = GridBagConstraints.VERTICAL;
		containerPanel.add(this.leftPanel, constraints);
		constraints.gridx = 2;
		this.rightPanel = createSidePanel(false);
		containerPanel.add(this.rightPanel, constraints);

		return containerPanel;
	}

	private JPanel createSidePanel(boolean horizontal)
	{
		JPanel panel = new JPanel();
		if (horizontal)
		{
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		}
		else
		{
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		}
		return panel;
	}

	private <T> T directionPanelMethod(Direction direction, Function<JPanel, T> normal, Function<JPanel, T> inverse)
	{
		JPanel panel = this.getPanel(direction);
		if (this.forwardSequential(direction))
		{
			return normal.apply(panel);
		}
		else
		{
			return inverse.apply(panel);
		}
	}

	// Since java can't deduce void >:(
	private void directionPanelMethodVoid(Direction direction, Consumer<JPanel> normal, Consumer<JPanel> inverse)
	{
		JPanel panel = this.getPanel(direction);
		if (this.forwardSequential(direction))
		{
			normal.accept(panel);
		}
		else
		{
			inverse.accept(panel);
		}
	}

	private void addComponent(Direction direction, JComponent component, int index)
	{
		this.directionPanelMethodVoid(direction, 
			(panel) -> panel.add(component, index), 
			(panel) -> panel.add(component, panel.getComponentCount() - index)
		);
	}

	// Adds component to last
	private void addComponent(Direction direction, JComponent component)
	{
		this.directionPanelMethodVoid(direction, 
			(panel) -> panel.add(component), 
			(panel) -> panel.add(component, 0)
		);
	}

	private Component getComponent(Direction direction, int index)
	{
		return this.directionPanelMethod(direction,
			(panel) -> panel.getComponent(index),
			(panel) -> panel.getComponent(panel.getComponentCount() - 1 - index)
		);
	}

	// Gets last component
	private Component getComponent(Direction direction)
	{
		return this.directionPanelMethod(direction,
			(panel) -> panel.getComponent(panel.getComponentCount() - 1),
			(panel) -> panel.getComponent(0)
		);
	}

	private void removeComponent(Direction direction, int index)
	{
		this.directionPanelMethodVoid(direction,
			(panel) -> panel.remove(index), 
			(panel) -> panel.remove(panel.getComponentCount() - 1 - index)
		);
	}

	// Removes last component
	private void removeComponent(Direction direction)
	{
		this.directionPanelMethodVoid(direction,
			(panel) -> panel.remove(panel.getComponentCount() - 1), 
			(panel) -> panel.remove(0)
		);
	}

	private JPanel getPanel(Direction direction)
	{
		switch (direction)
		{
			case BOTTOM:
				return this.bottomPanel;
			case LEFT:
				return this.leftPanel;
			case RIGHT:
				return this.rightPanel;
			case TOP:
				return this.topPanel;
			default:
				return null;
		}
	}

	private GraphAxis createAxis(Direction direction)
	{
		switch (direction)
		{
			case BOTTOM:
				return new GraphAxisBottom(graph);
			case LEFT:
				return new GraphAxisLeft(graph);
			case RIGHT:
				return new GraphAxisRight(graph);
			case TOP:
				return new GraphAxisTop(graph);
			default:
				return null;
		}
	}
}
