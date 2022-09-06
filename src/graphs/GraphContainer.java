package graphs;

import javax.swing.JComponent;
import javax.swing.JPanel;

import data.DataScale;
import graphs.axis.Axis;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

/**
 * Represents the container of a graph and handles the components that surround it, such as labels and axises
 * Contains methods for adding components to every side of a Graph
**/

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

	private Graph graph;
	private DataScale defaultAxisScale;

	private JPanel containerPanel;
	private Axis topAxis;
	private Axis rightAxis;
	private Axis bottomAxis;
	private Axis leftAxis;

	public GraphContainer(Graph graph, DataScale defaultAxisScale)
	{
		this.setLayout(new BorderLayout());
		
		this.graph = graph;
		this.defaultAxisScale = defaultAxisScale;

		this.containerPanel = this.createContainerPanel();

		GridBagConstraints graphConstraints = new GridBagConstraints();
		graphConstraints.gridx = 1;
		graphConstraints.gridy = 1;
		graphConstraints.fill = GridBagConstraints.BOTH;
		graphConstraints.weightx = 1;
		graphConstraints.weighty = 1;

		this.containerPanel.add(this.graph, graphConstraints);

		this.add(this.containerPanel);
	}

	public GraphContainer(Graph graph)
	{
		this(graph, new DataScale(0, 0));
	}

	public Graph getGraph()
	{
		return this.graph;
	}

	public void lightCopy(GraphContainer container)
	{
		this.graph.lightCopy(container.graph);
		for (Direction d : Direction.values())
		{
			if (this.getAxis(d) != null && container.getAxis(d) != null)
			{
				this.getAxis(d).lightCopy(container.getAxis(d));
			}
		}
		this.defaultAxisScale.setX(container.defaultAxisScale.getX());
		this.defaultAxisScale.setY(container.defaultAxisScale.getY());
		this.repaint();
	}
	
	// Creates GridBag layout with four BoxLayout panels surrounding empty center
	private JPanel createContainerPanel()
	{
		JPanel panel = new JPanel(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		this.bottomAxis = new Axis(this, Direction.BOTTOM, this.defaultAxisScale.getX());
		panel.add(this.bottomAxis, constraints);
		constraints.gridy = 0;
		this.topAxis = new Axis(this, Direction.TOP, this.defaultAxisScale.getX());
		panel.add(this.topAxis, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		this.leftAxis = new Axis(this, Direction.LEFT, this.defaultAxisScale.getY());
		constraints.fill = GridBagConstraints.VERTICAL;
		panel.add(this.leftAxis, constraints);
		constraints.gridx = 2;
		this.rightAxis = new Axis(this, Direction.RIGHT, this.defaultAxisScale.getY());
		panel.add(this.rightAxis, constraints);

		return panel;
	}

	public Axis getAxis(Direction direction)
	{
		switch (direction)
		{
			case BOTTOM:
				return this.bottomAxis;
			case LEFT:
				return this.leftAxis;
			case RIGHT:
				return this.rightAxis;
			case TOP:
				return this.topAxis;
			default:
				return null;
		}
	}
}
