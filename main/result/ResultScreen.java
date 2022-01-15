package main.result;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Color;
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
import graphs.visualiser.ContinuousFunctionVisualiser;
import graphs.visualiser.InverseContinuousFunctionVisualiser;
import graphs.visualiser.VerticalLineVisualiser;
import hydraulics.ManningsFunction;
import hydraulics.ManningsModel;
import main.input.GraphEditDialog;
import main.input.GraphEditScreen;
import ui.Wrapper;

/**
 * Contains methods for constructing results screen
**/

public class ResultScreen extends JPanel
{
	private static final String INITIAL_N = "0.025";
	private static final String INITIAL_S = "1";
	private static final String INITIAL_Q = "1";
	private static final String INITIAL_LEVEL = "0";
	private static final String INITIAL_V = "0";
	private static final int DISPLAYED_SCALE = 3;

	private JFrame parent;

	private ManningsModel model;
	private ManningsFunction function;
	private Wrapper<BigDecimal> n;
	private Wrapper<BigDecimal> s;
	private Wrapper<BigDecimal> q;
	private Wrapper<BigDecimal> level;
	private Wrapper<BigDecimal> a;
	private Wrapper<BigDecimal> v;

	private JLabel levelLabel;
	private JLabel vLabel;
	private JLabel aLabel;

	private Graph manningsGraph;
	private GraphContainer manningsGraphContainer;
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
		this.level = new Wrapper<>(new BigDecimal(ResultScreen.INITIAL_LEVEL));
		this.a = new Wrapper<>(new BigDecimal(0));
		this.v = new Wrapper<>(new BigDecimal(ResultScreen.INITIAL_V));

		this.levelLabel = new JLabel();
		this.aLabel = new JLabel();
		this.vLabel = new JLabel();
		this.manningsGraph = this.createGraph();
		this.manningsGraphContainer = this.createGraphContainer(this.manningsGraph);
		this.manningsGraphEditDialog = new GraphEditDialog(this.parent, new GraphEditScreen(this.manningsGraphContainer));

		this.add(this.createSidePanel(), BorderLayout.WEST);

		this.add(this.manningsGraphContainer, BorderLayout.CENTER);
	}

	public void refresh()
	{
		this.function.updateUpperLimit();
		this.manningsGraph.repaint();
		if (this.level.value != null && this.a.value != null && this.v.value != null)
		{
			this.levelLabel.setText(this.level.value.setScale(ResultScreen.DISPLAYED_SCALE, RoundingMode.HALF_UP).toString());
			this.aLabel.setText(this.a.value.setScale(ResultScreen.DISPLAYED_SCALE, RoundingMode.HALF_UP).toString());
			this.vLabel.setText(this.v.value.setScale(ResultScreen.DISPLAYED_SCALE, RoundingMode.HALF_UP).toString());
		}
		else
		{
			this.levelLabel.setText("Overflow!");
			this.aLabel.setText("Overflow!");
			this.vLabel.setText("Overflow!");
		}
	}

	private JPanel createSidePanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(new JButton(this.manningsGraphEditDialog.createOpenAction("Edit Graph")));

		panel.add(this.numberEditPanel("Manning's Constant", this.n));
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(this.numberEditPanel("Channel Bed Slope", this.s));
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(this.numberEditPanel("Cross-Section Discharge (m^3/s)", this.q));
		panel.add(Box.createRigidArea(new Dimension(0, 10)));

		JButton calculate = new JButton(this.calculateAction());
		calculate.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(calculate);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(this.numberPanel("Water Level Elevation (m)", this.level.value.toString(), this.levelLabel));
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(this.numberPanel("Cross-Section Area (m^2)", "0", this.aLabel));
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(this.numberPanel("Velocity (m/s)", this.v.value.toString(), this.vLabel));
		return panel;
	}

	private JPanel numberPanel(String name, String defaultString, JLabel numberLabel)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		// panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(new JLabel(String.format("%s: ", name)));
		numberLabel.setText(defaultString);
		panel.add(numberLabel);
		return panel;
	}

	private JPanel numberEditPanel(String name, Wrapper<BigDecimal> number)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		// panel.setAlignmentX(Component.LEFT_ALIGNMENT);
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
				ManningsModel model = ResultScreen.this.model;
				double q = ResultScreen.this.q.value.doubleValue();
				model.setN(ResultScreen.this.n.value);
				model.setS(ResultScreen.this.s.value);
				double level = model.calcWaterLevel(q, ResultScreen.this.modelStep(ResultScreen.DISPLAYED_SCALE));

				if (model.withinBounds(level))
				{
					ResultScreen.this.level.value = new BigDecimal(level);
					ResultScreen.this.a.value = new BigDecimal(model.calcArea(level));
					ResultScreen.this.v.value = new BigDecimal(model.calcVelocity(q, level));
				}
				else
				{
					ResultScreen.this.level.value = null;
					ResultScreen.this.a.value = null;
					ResultScreen.this.v.value = null;
				}
				
				ResultScreen.this.refresh();
			}
		};
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
		graph.setLinearPlane(new Range(0, 1000), new Range(0, 10));
		graph.fitGridPlane(100, 2);
		graph.getGraphComponents().add(new InverseContinuousFunctionVisualiser(graph, this.function));
		graph.getGraphComponents().add(new VerticalLineVisualiser(graph, 493.66637)); // Line that the function should touch and end
		// graph.getGraphComponents().add(new InverseContinuousFunctionVisualiser(graph, new Parabola(1, 0, 0)));
		return graph;
	}

	// Value of step of model should be 10^-(scale of displayed values + 1)
	private double modelStep(int scale)
	{
		return Math.pow(0.1, scale + 1);
	}
}
