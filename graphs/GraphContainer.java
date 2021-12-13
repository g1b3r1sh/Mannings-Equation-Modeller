package graphs;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.util.function.Consumer;
import java.awt.GridBagConstraints;
import java.awt.Component;

// Useful site: https://thebadprogrammer.com/swing-layout-manager-sizing/
// TODO: Enable scrollpane (scale axis to pane, zoom in/zoom out features)
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

	public void addAxis(Direction direction)
	{
		if (!this.containsAxis(direction))
		{
			this.addComponent(direction, this.createAxis(direction), 0);
		}
	}

	public void removeAxis(Direction direction)
	{
		if (this.containsAxis(direction))
		{
			this.removeComponent(direction, 0);
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

	private void directionPanelMethod(Direction direction, Consumer<JPanel> normal, Consumer<JPanel> inverse)
	{
		JPanel panel = this.getPanel(direction);
		switch (direction)
		{
			case BOTTOM:
			case RIGHT:
				normal.accept(panel);
				return;
			case LEFT:
			case TOP:
				inverse.accept(panel);
				return;
		}
	}

	private void addComponent(Direction direction, JComponent component, int index)
	{
		this.directionPanelMethod(direction, 
			(panel) -> panel.add(component, index), 
			(panel) -> panel.add(component, panel.getComponentCount() - 1 - index)
		);
	}

	// Adds component to last
	private void addComponent(Direction direction, JComponent component)
	{
		this.directionPanelMethod(direction, 
			(panel) -> panel.add(component), 
			(panel) -> panel.add(component, 0)
		);
	}

	// Gets last component
	private Component getComponent(Direction direction)
	{
		JPanel panel = this.getPanel(direction);
		if (direction == Direction.BOTTOM || direction == Direction.RIGHT)
		{
			return panel.getComponent(panel.getComponentCount() - 1);
		}
		else
		{
			return panel.getComponent(0);
		}
	}

	private void removeComponent(Direction direction, int index)
	{
		this.directionPanelMethod(direction,
			(panel) -> panel.remove(index), 
			(panel) -> panel.remove(panel.getComponentCount() - 1 - index)
		);
	}

	// Removes last component
	private void removeComponent(Direction direction)
	{
		this.directionPanelMethod(direction,
			(panel) -> panel.remove(panel.getComponentCount() - 1), 
			(panel) -> panel.remove(0)
		);
	}

	private boolean containsAxis(Direction direction)
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
