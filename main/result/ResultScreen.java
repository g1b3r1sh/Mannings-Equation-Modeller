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
import javax.swing.text.JTextComponent;

import data.DataScale;
import data.Range;
import data.functions.DiscreteData;
import graphs.Graph;
import graphs.GraphContainer;
import graphs.GraphController;
import graphs.GraphContainer.Direction;
import graphs.visualiser.InverseContinuousFunctionVisualiser;
import main.dialogs.GraphEditDialog;
import main.dialogs.GraphEditScreen;
import main.dialogs.SwingWorkerDialog;
import spinner.SpinnerController;
import spinner.SpinnerWrapperController;
import utility.Wrapper;

/**
 * Contains methods for constructing results screen
**/

public class ResultScreen extends JPanel
{
	private static Range DEFAULT_X_RANGE = new Range(0, 10);
	private static Range DEFAULT_Y_RANGE = new Range(0, 5);

	private JFrame parent;

	private ManningsResultModel resultModel;

	private Graph manningsGraph;
	private GraphContainer manningsGraphContainer;
	private GraphController manningsGraphController;
	private GraphEditDialog manningsGraphEditDialog;
	private SwingWorkerDialog workerDialog;

	private SpinnerController<Integer> outputScaleController;

	private ResultsVisualiser resultsVisualiser;

	public ResultScreen(JFrame parent, DiscreteData<BigDecimal, BigDecimal> data)
	{
		super();
		this.setLayout(new BorderLayout(10, 10));

		this.parent = parent;
		this.resultModel = new ManningsResultModel(data);
		this.outputScaleController = new SpinnerWrapperController<>(this.resultModel.getOutputScale());

		this.manningsGraph = ResultScreen.createGraph(this.resultModel);
		this.manningsGraphContainer = ResultScreen.createGraphContainer(this.manningsGraph);
		this.manningsGraphController = new GraphController(this.manningsGraphContainer);

		this.resultsVisualiser = new ResultsVisualiser(this.manningsGraph);
		this.manningsGraph.getGraphComponents().add(this.resultsVisualiser);

		this.manningsGraphEditDialog = new GraphEditDialog(this.parent, new GraphEditScreen(this.manningsGraphContainer));
		this.manningsGraphEditDialog.addPropertyChangeListener(this.manningsGraphController);
		this.workerDialog = new SwingWorkerDialog(this.parent, "Calculate", "Calculating Water Level...");

		this.add(this.createSidePanel(), BorderLayout.WEST);
		this.add(this.manningsGraphContainer, BorderLayout.CENTER);
	}

	public void openGraphEditDialog()
	{
		this.manningsGraphEditDialog.open();
	}

	protected <T> T runWorker(SwingWorker<T, ?> worker)
	{
		return this.workerDialog.runWorker(worker);
	}

	protected void processResults(ManningsResultModel.Result[] results)
	{
		this.resultsVisualiser.setResults(results);
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

		panel.add(this.constantEditPanel("Manning's Constant", this.resultModel.getN()));
		panel.add(ResultScreen.sidePadding());
		panel.add(this.constantEditPanel("Channel Bed Slope", this.resultModel.getS()));
		panel.add(ResultScreen.sidePadding());
		panel.add(ResultScreen.integerSpinnerPanel("Output Scale: ", this.outputScaleController, ManningsResultModel.MIN_DISPLAY_SCALE, ManningsResultModel.MAX_DISPLAY_SCALE, 1));

		return panel;
	}

	private JPanel constantEditPanel(String name, Wrapper<BigDecimal> wrapper)
	{
		return ResultScreen.numberEditPanel(this, name, wrapper::get, wrapper::set, (value) ->
		{
			ResultScreen.this.resultModel.updateModelConstants();
			ResultScreen.this.resultsVisualiser.repaint();
		});
	}

	private JTabbedPane createOutputPane()
	{
		JTabbedPane pane = new JTabbedPane();
		pane.addTab("Single Output", new SingleOutputPanel(this, this.resultModel));
		pane.addTab("Table Output", new TableOutputPanel(this, this.resultModel));
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

	protected static JPanel numberDisplayPanel(String name, String defaultString, JTextComponent numberLabel)
	{
		JPanel panel = ResultScreen.labelPanel();
		panel.add(new JLabel(String.format("%s: ", name)));
		numberLabel.setText(defaultString);
		panel.add(numberLabel);
		return panel;
	}

	private static JPanel numberEditPanel(Component parent, String name, Supplier<BigDecimal> get, Consumer<BigDecimal> set, Consumer<BigDecimal> setSuccess)
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

	protected static JPanel numberEditPanel(Component parent, String name, Wrapper<BigDecimal> wrapper)
	{
		return ResultScreen.numberEditPanel(parent, name, wrapper::get, wrapper::set, value -> {});
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

	private static GraphContainer createGraphContainer(Graph graph)
	{
		GraphContainer container = new GraphContainer(graph, new DataScale(2, 1));
		container.getAxis(Direction.TOP).addName("Cross-Section Discharge vs Water Elevation");
		container.getAxis(Direction.BOTTOM).addTickmarks();
		container.getAxis(Direction.BOTTOM).addNumbers();
		container.getAxis(Direction.BOTTOM).addName("Discharge, Q");
		container.getAxis(Direction.LEFT).addTickmarks();
		container.getAxis(Direction.LEFT).addNumbers();
		container.getAxis(Direction.LEFT).addName("Water Elevation");
		return container;	
	}

	private static Graph createGraph(ManningsResultModel resultModel)
	{
		Graph graph = new Graph();
		graph.setLinearPlane(ResultScreen.DEFAULT_X_RANGE, ResultScreen.DEFAULT_Y_RANGE);
		graph.fitGridPlane(2, 1);
		graph.getGraphComponents().add(new InverseContinuousFunctionVisualiser(graph, resultModel.getFunction()));
		return graph;
	}
}
