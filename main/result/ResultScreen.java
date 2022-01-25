package main.result;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.concurrent.ExecutionException;
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
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;

import data.DataPrecision;
import data.MapDiscreteData;
import data.Parabola;
import graphs.Graph;
import graphs.GraphContainer;
import graphs.Range;
import graphs.GraphContainer.Direction;
import graphs.visualiser.InverseContinuousFunctionVisualiser;
import graphs.visualiser.VerticalLineVisualiser;
import main.dialogs.GraphController;
import main.dialogs.GraphEditDialog;
import main.dialogs.GraphEditScreen;
import main.dialogs.SwingWorkerDialog;
import ui.SpinnerController;
import ui.SpinnerWrapperController;
import ui.Wrapper;

/**
 * Contains methods for constructing results screen
**/

public class ResultScreen extends JPanel
{
	private static final int DEFAULT_DISPLAYED_SCALE = 3;
	private static final int MIN_DISPLAYED_SCALE = 0;
	private static final int MAX_DISPLAYED_SCALE = 9;
	private static final BigDecimal DEFAULT_MIN_DISCHARGE = new BigDecimal(100);
	private static final BigDecimal DEFAULT_MAX_DISCHARGE = new BigDecimal(1000);
	private static final int DEFAULT_NUM_DISCHARGE_ROWS = 5;
	private static final int MIN_NUM_DISCHARGE_ROWS = 0;

	private JFrame parent;

	private ResultScreenController controller;

	private JLabel levelLabel;
	private JLabel vLabel;
	private JLabel aLabel;
	private JLabel errorLabel;
	private EnumMap<ResultScreenController.ModelError, JLabel> errorLabels;

	private Graph manningsGraph;
	private GraphContainer manningsGraphContainer;
	private GraphController manningsGraphController;
	private GraphEditDialog manningsGraphEditDialog;
	private SwingWorkerDialog workerDialog;

	private Wrapper<BigDecimal> dischargeLower;
	private Wrapper<BigDecimal> dischargeUpper;
	private Wrapper<Integer> numDischargeRows;
	private Wrapper<Integer> outputPrecision;
	private SpinnerController<Integer> numDischargeRowsController;
	private SpinnerController<Integer> outputPrecisionController;

	private ResultsTableModel tableModel;
	private ResultsVisualiser resultsVisualiser;

	public ResultScreen(MapDiscreteData<BigDecimal, BigDecimal> data, JFrame parent)
	{
		super();
		this.parent = parent;

		this.controller = new ResultScreenController(data);

		this.setLayout(new BorderLayout(10, 10));

		this.levelLabel = new JLabel();
		this.aLabel = new JLabel();
		this.vLabel = new JLabel();
		this.errorLabel = new JLabel("");
		this.errorLabel.setForeground(Color.RED);
		this.errorLabel.setVisible(false);
		this.errorLabels = this.createErrorLabelsMap();

		this.manningsGraph = this.createGraph();
		this.manningsGraphContainer = this.createGraphContainer(this.manningsGraph);
		this.manningsGraphController = new GraphController(this.manningsGraphContainer);
		this.manningsGraphEditDialog = new GraphEditDialog(this.parent, new GraphEditScreen(this.manningsGraphContainer));
		this.manningsGraphEditDialog.addPropertyChangeListener(this.manningsGraphController);

		this.dischargeLower = new Wrapper<>(ResultScreen.DEFAULT_MIN_DISCHARGE);
		this.dischargeUpper = new Wrapper<>(ResultScreen.DEFAULT_MAX_DISCHARGE);
		this.numDischargeRows = new Wrapper<>(ResultScreen.DEFAULT_NUM_DISCHARGE_ROWS);
		this.outputPrecision = new Wrapper<>(ResultScreen.DEFAULT_DISPLAYED_SCALE);
		this.numDischargeRowsController = new SpinnerWrapperController<>(this.numDischargeRows);
		this.outputPrecisionController = new SpinnerWrapperController<>(this.outputPrecision);

		this.tableModel = new ResultsTableModel();
		this.resultsVisualiser = new ResultsVisualiser(this.manningsGraph);
		this.manningsGraph.getGraphComponents().add(this.resultsVisualiser);

		this.workerDialog = new SwingWorkerDialog(this.parent, "Calculate", "Calculating Water Level...");

		this.add(this.createSidePanel(), BorderLayout.WEST);
		this.add(this.manningsGraphContainer, BorderLayout.CENTER);

		this.refreshGraph();
	}

	private void refreshOutput()
	{
		this.refreshOutputLabels();
		this.refreshErrorMessage();
	}

	private void refreshGraph()
	{
		this.controller.getFunction().update();
		this.manningsGraph.repaint();
	}

	private void refreshOutputLabels()
	{
		BigDecimal level = this.controller.getLevel();
		BigDecimal a = this.controller.getA();
		BigDecimal v = this.controller.getV();
		if (level != null && a != null && v != null)
		{
			this.levelLabel.setText(level.toString());
			this.aLabel.setText(a.toString());
			this.vLabel.setText(v.toString());
		}
		else
		{
			this.levelLabel.setText("");
			this.aLabel.setText("");
			this.vLabel.setText("");
		}
	}

	private void refreshErrorMessage()
	{
		switch (this.controller.getError())
		{
			case CONSTANTS_NOT_SET:
				this.showErrorMessage("Constants not set!");
				break;
			case DISCHARGE_UNDERFLOW:
				this.showErrorMessage("Discharge too low!");
				break;
			case NOT_ENOUGH_DATA:
				this.showErrorMessage("Not enough data points!");
				break;
			case NONE:
			default:
				this.hideErrorMessage();
		}
	}

	private void showErrorMessage(String message)
	{
		this.errorLabel.setText(message);
		this.errorLabel.setVisible(true);
	}

	private void hideErrorMessage()
	{
		this.errorLabel.setText("");
		this.errorLabel.setVisible(false);
	}

	private void processResults(ResultScreenController.Result[] results)
	{
		this.tableModel.setData(results);
		this.showErrors(this.createErrorSet(results));
		this.resultsVisualiser.setResults(results);
		this.manningsGraph.repaint();
	}

	private EnumSet<ResultScreenController.ModelError> createErrorSet(ResultScreenController.Result[] results)
	{
		EnumSet<ResultScreenController.ModelError> errors = EnumSet.noneOf(ResultScreenController.ModelError.class);
		errors.add(ResultScreenController.ModelError.NONE);
		for (ResultScreenController.Result result : results)
		{
			errors.add(result.getError());
		}
		return errors;
	}

	private void showErrors(EnumSet<ResultScreenController.ModelError> errors)
	{
		for (ResultScreenController.ModelError error : ResultScreenController.ModelError.values())
		{
			if (this.errorLabels.containsKey(error))
			{
				this.errorLabels.get(error).setVisible(errors.contains(error));
				
			}
		}
	}

	private EnumMap<ResultScreenController.ModelError, JLabel> createErrorLabelsMap()
	{
		EnumMap<ResultScreenController.ModelError, JLabel> labels = new EnumMap<>(ResultScreenController.ModelError.class);
		labels.put(ResultScreenController.ModelError.CONSTANTS_NOT_SET, this.createErrorLabel("Constants not set!"));
		labels.put(ResultScreenController.ModelError.DISCHARGE_UNDERFLOW, this.createErrorLabel("Discharge too low!"));
		labels.put(ResultScreenController.ModelError.NOT_ENOUGH_DATA, this.createErrorLabel("Not enough data points!"));
		labels.put(ResultScreenController.ModelError.INVALID_DISCHARGE_RANGE, this.createErrorLabel("Min value cannot be greater than max value!"));
		return labels;
	}

	private JLabel createErrorLabel(String message)
	{
		JLabel label = new JLabel(message);
		label.setVisible(false);
		label.setForeground(Color.RED);
		return label;
	}

	private JPanel createSidePanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(this.centerAlignX(new JButton(this.manningsGraphEditDialog.createOpenAction("Edit Graph"))));
		panel.add(this.createInputPanel());
		panel.add(this.centerAlignX(new JButton(this.calculateAction())));
		panel.add(this.centerAlignX(new JButton(this.calculateResultsAction())));
		panel.add(this.createOutputPanel());

		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(this.createErrorPanel());
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(this.createTablePane(this.createOutputTable()));

		return panel;
	}

	private JComponent centerAlignX(JComponent component)
	{
		component.setAlignmentX(Component.CENTER_ALIGNMENT);
		return component;
	}

	private JPanel createInputPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(this.parameterEditPanel("Manning's Constant", () -> this.controller.getN(), (n) -> this.controller.setN(n)));
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(this.parameterEditPanel("Channel Bed Slope", () -> this.controller.getS(), (s) -> this.controller.setS(s)));
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(this.numberEditPanel("Cross-Section Discharge (m^3/s)", () -> this.controller.getQ(), (q) -> this.controller.setQ(q)));
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(this.integerSpinnerPanel("Output Scale: ", this.outputPrecisionController, ResultScreen.MIN_DISPLAYED_SCALE, ResultScreen.MAX_DISPLAYED_SCALE, 1));
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(this.integerSpinnerPanel("Output Rows: ", this.numDischargeRowsController, ResultScreen.MIN_NUM_DISCHARGE_ROWS, null, 1));
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(this.numberEditPanel("Discharge Min", () -> this.dischargeLower.get(), (discharge) -> this.dischargeLower.set(discharge)));
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(this.numberEditPanel("Discharge Max", () -> this.dischargeUpper.get(), (discharge) -> this.dischargeUpper.set(discharge)));
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		return panel;
	}

	private JPanel createOutputPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(this.componentPanel(this.errorLabel));
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(this.numberDisplayPanel("Water Level Elevation (m)", "", this.levelLabel));
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(this.numberDisplayPanel("Cross-Section Area (m^2)", "", this.aLabel));
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(this.numberDisplayPanel("Velocity (m/s)", "", this.vLabel));
		return panel;
	}

	private JPanel labelPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		return panel;
	}

	private JPanel componentPanel(JComponent component)
	{
		JPanel panel = this.labelPanel();
		panel.add(component);
		return panel;
	}

	private JPanel numberDisplayPanel(String name, String defaultString, JLabel numberLabel)
	{
		JPanel panel = this.labelPanel();

		panel.add(new JLabel(String.format("%s: ", name)));
		numberLabel.setText(defaultString);
		panel.add(numberLabel);
		return panel;
	}

	private JPanel numberEditPanel(String name, Supplier<BigDecimal> get, Consumer<BigDecimal> set, Consumer<BigDecimal> setSuccess)
	{
		JPanel panel = this.labelPanel();

		JLabel numberText = new JLabel(get.get().toString());
		JButton editButton = new JButton(new AbstractAction("Edit")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String output = JOptionPane.showInputDialog(ResultScreen.this, "Input new value", get.get().toString());
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

	private JPanel numberEditPanel(String name, Supplier<BigDecimal> get, Consumer<BigDecimal> set)
	{
		return this.numberEditPanel(name, get, set, (value) -> {});
	}

	private JPanel parameterEditPanel(String name, Supplier<BigDecimal> get, Consumer<BigDecimal> set)
	{
		return this.numberEditPanel(name, get, set, (value) ->
		{
			ResultScreen.this.controller.updateModelConstants();
			ResultScreen.this.refreshGraph();
		});
	}

	private JPanel integerSpinnerPanel(String label, SpinnerController<Integer> controller, Integer min, Integer max, int step)
	{
		JPanel panel = this.labelPanel();
		panel.add(new JLabel(label));
		panel.add(this.createIntegerSpinner(controller, min, max, step));
		return panel;
	}

	private JSpinner createIntegerSpinner(SpinnerController<Integer> controller, Integer min, Integer max, Number step)
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

	private JTable createOutputTable()
	{
		JTable table = new JTable(this.tableModel);
		table.setCellSelectionEnabled(true);
		table.getTableHeader().setReorderingAllowed(false);
		return table;
	}

	private JScrollPane createTablePane(JTable table)
	{
		JScrollPane pane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// Default width: 350
		pane.setPreferredSize(new Dimension(350, 1080));
		return pane;
	}

	private JPanel createErrorPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		for (ResultScreenController.ModelError error : this.errorLabels.keySet())
		{
			panel.add(this.errorLabels.get(error));
		}
		return panel;
	}

	private Action calculateAction()
	{
		return new AbstractAction("Calculate")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ResultScreen.this.controller.updateModelConstants();
				ResultScreen.this.calcOutputValues();
				ResultScreen.this.refreshOutput();
			}
		};
	}

	private void calcOutputValues()
	{
		this.workerDialog.open(this.controller.createWaterLevelWorker(this.outputPrecision.value));
	}

	private Action calculateResultsAction()
	{
		return new AbstractAction("Calculate Table")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ResultScreen.this.controller.updateModelConstants();
				ResultScreenController.Result[] results = ResultScreen.this.calcResults();
				if (results != null)
				{
					ResultScreen.this.processResults(results);
				}
			}
		};
	}

	private ResultScreenController.Result[] calcResults()
	{
		SwingWorker<ResultScreenController.Result[], ?> worker = this.controller.createResultsWorker
		(
			this.dischargeLower.value.doubleValue(),
			this.dischargeUpper.value.doubleValue(),
			this.numDischargeRows.value,
			this.outputPrecision.value
		);
		this.workerDialog.open(worker);
		if (worker.isCancelled())
		{
			return null;
		}
		else
		{
			try
			{
				return worker.get();
			}
			catch (InterruptedException | ExecutionException e)
			{
				e.printStackTrace();
				return null;
			}
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
		graph.getGraphComponents().add(new InverseContinuousFunctionVisualiser(graph, this.controller.getFunction()));
		// graph.getGraphComponents().add(new VerticalLineVisualiser(graph, 493.66637)); // Line that the default function should touch and end
		// graph.getGraphComponents().add(new InverseContinuousFunctionVisualiser(graph, new Parabola(1, 0, 0)));
		return graph;
	}

}
