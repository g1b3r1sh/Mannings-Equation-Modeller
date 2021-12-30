package graphs;

import javax.swing.JComponent;
import javax.swing.JPanel;

import data.DataPrecision;
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
	private DataPrecision defaultPrecision;

	private JPanel containerPanel;
	private Axis topAxis;
	private Axis rightAxis;
	private Axis bottomAxis;
	private Axis leftAxis;

	//private JScrollPane scrollPane;

	public GraphContainer(Graph graph, DataPrecision defaultPrecision)
	{
		this.setLayout(new BorderLayout());
		
		this.graph = graph;
		this.defaultPrecision = defaultPrecision;

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
	
	// Creates GridBag layout with four BoxLayout panels surrounding empty center
	private JPanel createContainerPanel()
	{
		containerPanel = new JPanel(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		this.bottomAxis = new Axis(this, Direction.BOTTOM, this.defaultPrecision.getX());
		containerPanel.add(this.bottomAxis, constraints);
		constraints.gridy = 0;
		this.topAxis = new Axis(this, Direction.TOP, this.defaultPrecision.getX());
		containerPanel.add(this.topAxis, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		this.leftAxis = new Axis(this, Direction.LEFT, this.defaultPrecision.getY());
		constraints.fill = GridBagConstraints.VERTICAL;
		containerPanel.add(this.leftAxis, constraints);
		constraints.gridx = 2;
		this.rightAxis = new Axis(this, Direction.RIGHT, this.defaultPrecision.getY());
		containerPanel.add(this.rightAxis, constraints);

		return containerPanel;
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
