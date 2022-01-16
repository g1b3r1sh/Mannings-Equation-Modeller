package main.result;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import data.DataPrecision;
import data.MapDiscreteData;
import data.Parabola;
import graphs.Graph;
import graphs.GraphContainer;
import graphs.Range;
import graphs.GraphContainer.Direction;
import graphs.visualiser.InverseContinuousFunctionVisualiser;
import graphs.visualiser.VerticalLineVisualiser;
import hydraulics.ManningsFunction;
import hydraulics.ManningsModel;
import main.dialogs.GraphController;
import main.dialogs.GraphEditDialog;
import main.dialogs.GraphEditScreen;
import ui.Wrapper;

/**
 * Contains methods for constructing results screen
**/

public class ResultScreen extends JPanel
{
	private static final String INITIAL_N = "0.025";
	private static final String INITIAL_S = "1";
	private static final String INITIAL_Q = "1";
	private static final int DISPLAYED_SCALE = 6;

	private JFrame parent;

	private ManningsModel model;
	private ManningsFunction function;
	private Wrapper<BigDecimal> n;
	private Wrapper<BigDecimal> s;
	private Wrapper<BigDecimal> q;
	private Wrapper<BigDecimal> level;
	private Wrapper<BigDecimal> a;
	private Wrapper<BigDecimal> v;
	private boolean overflowed;

	private JLabel levelLabel;
	private JLabel vLabel;
	private JLabel aLabel;
	private JLabel overflowLabel;

	private Graph manningsGraph;
	private GraphContainer manningsGraphContainer;
	private GraphController manningsGraphController;
	private GraphEditDialog manningsGraphEditDialog;

	public ResultScreen(MapDiscreteData<BigDecimal, BigDecimal> data, JFrame parent)
	{
		super();
		this.parent = parent;

		this.setLayout(new BorderLayout(10, 10));
		this.model = new ManningsModel(data);
		this.function = new ManningsFunction(this.model);
		this.n = new Wrapper<>(new BigDecimal(ResultScreen.INITIAL_N));
		this.s = new Wrapper<>(new BigDecimal(ResultScreen.INITIAL_S));
		this.q = new Wrapper<>(new BigDecimal(ResultScreen.INITIAL_Q));
		this.level = new Wrapper<>(null);
		this.a = new Wrapper<>(null);
		this.v = new Wrapper<>(null);

		this.levelLabel = new JLabel();
		this.aLabel = new JLabel();
		this.vLabel = new JLabel();
		this.overflowLabel = new JLabel("Overflow!");
		this.overflowLabel.setForeground(Color.RED);
		this.overflowLabel.setVisible(false);
		this.overflowed = false;
		this.manningsGraph = this.createGraph();
		this.manningsGraphContainer = this.createGraphContainer(this.manningsGraph);
		this.manningsGraphController = new GraphController(this.manningsGraphContainer);
		this.manningsGraphEditDialog = new GraphEditDialog(this.parent, new GraphEditScreen(this.manningsGraphContainer));
		this.manningsGraphEditDialog.addPropertyChangeListener(this.manningsGraphController);

		this.add(this.createSidePanel(), BorderLayout.WEST);

		this.add(this.manningsGraphContainer, BorderLayout.CENTER);
	}

	public void refresh()
	{
		this.function.updateRange();
		this.manningsGraph.repaint();
		if (this.level.value != null && this.a.value != null && this.v.value != null)
		{
			this.levelLabel.setText(this.level.value.setScale(ResultScreen.DISPLAYED_SCALE, RoundingMode.HALF_UP).toString());
			this.aLabel.setText(this.a.value.setScale(ResultScreen.DISPLAYED_SCALE, RoundingMode.HALF_UP).toString());
			this.vLabel.setText(this.v.value.setScale(ResultScreen.DISPLAYED_SCALE, RoundingMode.HALF_UP).toString());
		}
		else
		{
			this.levelLabel.setText("");
			this.aLabel.setText("");
			this.vLabel.setText("");
		}
		this.overflowLabel.setVisible(this.overflowed);
	}

	private JPanel createSidePanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JButton editButton = new JButton(this.manningsGraphEditDialog.createOpenAction("Edit Graph"));
		editButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(editButton);

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
		inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		inputPanel.add(this.numberEditPanel("Manning's Constant", this.n));
		inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		inputPanel.add(this.numberEditPanel("Channel Bed Slope", this.s));
		inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		inputPanel.add(this.numberEditPanel("Cross-Section Discharge (m^3/s)", this.q));
		inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(inputPanel);

		JButton calculate = new JButton(this.calculateAction());
		calculate.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(calculate);

		JPanel outputPanel = new JPanel();
		outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));
		outputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		outputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		outputPanel.add(this.labelPanel(this.overflowLabel));
		outputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		outputPanel.add(this.numberDisplayPanel("Water Level Elevation (m)", "", this.levelLabel));
		outputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		outputPanel.add(this.numberDisplayPanel("Cross-Section Area (m^2)", "", this.aLabel));
		outputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		outputPanel.add(this.numberDisplayPanel("Velocity (m/s)", "", this.vLabel));
		panel.add(outputPanel);

		return panel;
	}

	private JPanel labelPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		return panel;
	}

	private JPanel labelPanel(JLabel label)
	{
		JPanel panel = this.labelPanel();
		panel.add(label);
		return panel;
	}

	private JPanel numberDisplayPanel(String name, String defaultString, JLabel numberLabel)
	{
		JPanel panel = this.labelPanel(new JLabel(String.format("%s: ", name)));

		numberLabel.setText(defaultString);
		panel.add(numberLabel);
		return panel;
	}

	private JPanel numberEditPanel(String name, Wrapper<BigDecimal> number)
	{
		JPanel panel = this.labelPanel();

		JLabel numberText = new JLabel(number.value.toString());
		JButton editButton = new JButton(new AbstractAction("Edit")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String output = JOptionPane.showInputDialog(ResultScreen.this, "Input new value", number.value.toString());
				if (output != null)
				{
					try
					{
						number.value = new BigDecimal(output);
						numberText.setText(number.value.toString());
					}
					catch (NumberFormatException exception) {}
				}
			}
		});

		panel.add(editButton);
		panel.add(Box.createRigidArea(new Dimension(10, 0)));
		panel.add(new JLabel(String.format("%s: ", name)));
		panel.add(numberText);
		return panel;
	}

	private Action calculateAction()
	{
		return new AbstractAction("Calculate")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ResultScreen.this.updateModelConstants();
				ResultScreen.this.setOutputValues(ResultScreen.this.q.value.doubleValue());
				
				ResultScreen.this.refresh();
			}
		};
	}

	private void updateModelConstants()
	{
		this.model.setN(this.n.value);
		this.model.setS(this.s.value);
	}

	private void setOutputValues(double q)
	{
		double level = this.model.calcWaterLevel(q, this.modelStep(ResultScreen.DISPLAYED_SCALE));

		if (this.model.withinBounds(level))
		{
			this.level.value = new BigDecimal(level);
			this.a.value = new BigDecimal(this.model.calcArea(level));
			this.v.value = new BigDecimal(this.model.calcVelocity(q, level));
		}
		else
		{
			this.level.value = null;
			this.a.value = null;
			this.v.value = null;
		}
	}

	private GraphContainer createGraphContainer(Graph graph)
	{
		GraphContainer container = new GraphContainer(graph, new DataPrecision(1, 1));
		container.getAxis(Direction.BOTTOM).addTickmarks();
		container.getAxis(Direction.BOTTOM).addNumbers();
		container.getAxis(Direction.BOTTOM).addName("Discharge, Q");
		container.getAxis(Direction.LEFT).addTickmarks();
		container.getAxis(Direction.LEFT).addNumbers();
		container.getAxis(Direction.LEFT).addName("Water Elevation");
		return container;	
	}

	private Graph createGraph()
	{
		Graph graph = new Graph();
		// graph.setLinearPlane(new Range(0, 10), new Range(0, 5));
		graph.setLinearPlane(new Range(0, 500), new Range(0, 10));
		graph.fitGridPlane(100, 2);
		graph.getGraphComponents().add(new InverseContinuousFunctionVisualiser(graph, this.function));
		// graph.getGraphComponents().add(new VerticalLineVisualiser(graph, 493.66637)); // Line that the default function should touch and end
		// graph.getGraphComponents().add(new InverseContinuousFunctionVisualiser(graph, new Parabola(1, 0, 0)));
		return graph;
	}
}
