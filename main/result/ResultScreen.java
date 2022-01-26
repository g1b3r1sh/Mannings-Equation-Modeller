package main.result;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;

import data.DataPrecision;
import data.Range;
import data.functions.MapDiscreteData;
import graphs.Graph;
import graphs.GraphContainer;
import graphs.GraphController;
import graphs.GraphContainer.Direction;
import graphs.visualiser.InverseContinuousFunctionVisualiser;
import main.dialogs.GraphEditDialog;
import main.dialogs.GraphEditScreen;
import main.dialogs.SwingWorkerDialog;
import spinner.SpinnerController;

/**
 * Contains methods for constructing results screen
**/

public class ResultScreen extends JPanel
{
	private static final int MIN_DISPLAYED_SCALE = 0;
	private static final int MAX_DISPLAYED_SCALE = 9;

	private JFrame parent;

	private ManningsModelController controller;

	private Graph manningsGraph;
	private GraphContainer manningsGraphContainer;
	private GraphController manningsGraphController;
	private GraphEditDialog manningsGraphEditDialog;
	private SwingWorkerDialog workerDialog;

	private SpinnerController<Integer> outputPrecisionController;

	private ResultsVisualiser resultsVisualiser;

	public ResultScreen(MapDiscreteData<BigDecimal, BigDecimal> data, JFrame parent)
	{
		super();
		this.parent = parent;

		this.controller = new ManningsModelController(data);

		this.setLayout(new BorderLayout(10, 10));

		this.manningsGraph = this.createGraph();
		this.manningsGraphContainer = this.createGraphContainer(this.manningsGraph);
		this.manningsGraphController = new GraphController(this.manningsGraphContainer);
		this.manningsGraphEditDialog = new GraphEditDialog(this.parent, new GraphEditScreen(this.manningsGraphContainer));
		this.manningsGraphEditDialog.addPropertyChangeListener(this.manningsGraphController);

		this.outputPrecisionController = new SpinnerController<>(this.controller::getOutputPrecision, this.controller::setOutputPrecision);

		this.resultsVisualiser = new ResultsVisualiser(this.manningsGraph);
		this.manningsGraph.getGraphComponents().add(this.resultsVisualiser);

		this.workerDialog = new SwingWorkerDialog(this.parent, "Calculate", "Calculating Water Level...");

		this.add(this.createSidePanel(), BorderLayout.WEST);
		this.add(this.manningsGraphContainer, BorderLayout.CENTER);

		this.refreshGraph();
	}

	public void openGraphEditDialog()
	{
		this.manningsGraphEditDialog.open();
	}

	protected <T> T runWorker(SwingWorker<T, ?> worker)
	{
		return this.workerDialog.runWorker(worker);
	}

	protected void processResults(ManningsModelController.Result[] results)
	{
		this.resultsVisualiser.setResults(results);
		this.manningsGraph.repaint();
	}

	private void refreshGraph()
	{
		this.controller.getFunction().update();
		this.manningsGraph.repaint();
	}

	private JPanel createSidePanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(ResultScreen.createHeader("Input"));
		panel.add(ResultScreen.sideSeperator());
		panel.add(ResultScreen.sidePadding());
		panel.add(this.createInputPanel());
		panel.add(ResultScreen.sidePadding());
		panel.add(this.createOutputPane());

		return panel;
	}

	private JPanel createInputPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);

		panel.add(this.constantEditPanel("Manning's Constant", () -> this.controller.getN(), (n) -> this.controller.setN(n)));
		panel.add(ResultScreen.sidePadding());
		panel.add(this.constantEditPanel("Channel Bed Slope", () -> this.controller.getS(), (s) -> this.controller.setS(s)));
		panel.add(ResultScreen.sidePadding());
		panel.add(ResultScreen.integerSpinnerPanel("Output Scale: ", this.outputPrecisionController, ResultScreen.MIN_DISPLAYED_SCALE, ResultScreen.MAX_DISPLAYED_SCALE, 1));

		return panel;
	}

	private JPanel constantEditPanel(String name, Supplier<BigDecimal> get, Consumer<BigDecimal> set)
	{
		return ResultScreen.numberEditPanel(this, name, get, set, (value) ->
		{
			ResultScreen.this.controller.updateModelConstants();
			ResultScreen.this.refreshGraph();
		});
	}

	private JTabbedPane createOutputPane()
	{
		JTabbedPane pane = new JTabbedPane();
		pane.addTab("Single Output", new SingleOutputPanel(this, this.controller));
		pane.addTab("Table Output", new TableOutputPanel(this, this.controller));
		return pane;
	}

	protected static JLabel createHeader(String string)
	{
		JLabel label = new JLabel(string);
		label.setFont(label.getFont().deriveFont(15f));
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		return label;
	}

	protected static JButton sideButton(Action action)
	{
		JButton button = new JButton(action);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		return button;
	}

	protected static Component sidePadding()
	{
		return Box.createRigidArea(new Dimension(0, 5));
	}

	protected static JSeparator sideSeperator()
	{
		return new JSeparator()
		{
			@Override
			public Dimension getMaximumSize()
			{
				Dimension d = super.getMaximumSize();
				d.height = 1;
				return d;
			}
		};
	}

	protected static JPanel mainSidePanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		return panel;
	}

	protected static JPanel labelPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		return panel;
	}

	protected static JPanel componentPanel(JComponent component)
	{
		JPanel panel = ResultScreen.labelPanel();
		panel.add(component);
		return panel;
	}

	protected static JPanel numberDisplayPanel(String name, String defaultString, JLabel numberLabel)
	{
		JPanel panel = ResultScreen.labelPanel();
		panel.add(new JLabel(String.format("%s: ", name)));
		numberLabel.setText(defaultString);
		panel.add(numberLabel);
		return panel;
	}

	protected static JPanel numberEditPanel(Component parent, String name, Supplier<BigDecimal> get, Consumer<BigDecimal> set, Consumer<BigDecimal> setSuccess)
	{
		JPanel panel = ResultScreen.labelPanel();

		JLabel numberText = new JLabel(get.get().toString());
		JButton editButton = new JButton(new AbstractAction("Edit")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String output = JOptionPane.showInputDialog(parent, "Input new value", get.get().toString());
				if (output != null)
				{
					try
					{
						BigDecimal newValue = new BigDecimal(output);
						set.accept(newValue);
						setSuccess.accept(newValue);
						numberText.setText(get.get().toString());
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

	protected static JPanel numberEditPanel(Component parent, String name, Supplier<BigDecimal> get, Consumer<BigDecimal> set)
	{
		return ResultScreen.numberEditPanel(parent, name, get, set, (value) -> {});
	}

	protected static JPanel integerSpinnerPanel(String label, SpinnerController<Integer> controller, Integer min, Integer max, int step)
	{
		JPanel panel = ResultScreen.labelPanel();
		panel.add(new JLabel(label));
		panel.add(ResultScreen.createIntegerSpinner(controller, min, max, step));
		return panel;
	}

	protected static JSpinner createIntegerSpinner(SpinnerController<Integer> controller, Integer min, Integer max, Number step)
	{
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(controller.getValue(), min, max, step))
		{
			@Override
			public Dimension getMaximumSize()
			{
				Dimension size = super.getMaximumSize();
				size.height = super.getPreferredSize().height;
				return size;
			}
		};
		controller.setSpinner(spinner);
		return spinner;
	}

	private GraphContainer createGraphContainer(Graph graph)
	{
		GraphContainer container = new GraphContainer(graph, new DataPrecision(2, 1));
		container.getAxis(Direction.TOP).addName("Cross-Section Discharge vs Water Elevation");
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
		graph.setLinearPlane(new Range(0, 500), new Range(0, 10));
		graph.fitGridPlane(100, 2);
		graph.getGraphComponents().add(new InverseContinuousFunctionVisualiser(graph, this.controller.getFunction()));
		return graph;
	}
}
