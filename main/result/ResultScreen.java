package main.result;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ExecutionException;

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
import javax.swing.JSpinner;
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
import hydraulics.ManningsFunction;
import hydraulics.ManningsModel;
import main.dialogs.GraphController;
import main.dialogs.GraphEditDialog;
import main.dialogs.GraphEditScreen;
import main.dialogs.SwingWorkerDialog;
import ui.SpinnerController;
import ui.Wrapper;

/**
 * Contains methods for constructing results screen
**/

public class ResultScreen extends JPanel
{
	private static final String INITIAL_N = "0.025";
	private static final String INITIAL_S = "1";
	private static final String INITIAL_Q = "1";
	private static final int DEFAULT_DISPLAYED_SCALE = 3;
	private static final int MIN_DISPLAYED_SCALE = 0;
	private static final int MAX_DISPLAYED_SCALE = 9;

	private static enum ModelError
	{
		DISCHARGE_UNDERFLOW, CONSTANTS_NOT_SET, NOT_ENOUGH_DATA, NONE
	}

	private JFrame parent;

	private ManningsModel model;
	private ManningsFunction function;
	private Wrapper<BigDecimal> n;
	private Wrapper<BigDecimal> s;
	private Wrapper<BigDecimal> q;
	private Wrapper<BigDecimal> level;
	private Wrapper<BigDecimal> a;
	private Wrapper<BigDecimal> v;
	private ModelError error = ModelError.NONE;

	private JLabel levelLabel;
	private JLabel vLabel;
	private JLabel aLabel;
	private JLabel errorLabel;

	private Graph manningsGraph;
	private GraphContainer manningsGraphContainer;
	private GraphController manningsGraphController;
	private GraphEditDialog manningsGraphEditDialog;
	private SwingWorkerDialog workerDialog;

	private Wrapper<Integer> outputPrecision;
	private SpinnerController<Integer> outputPrecisionController;

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
		this.errorLabel = new JLabel("");
		this.errorLabel.setForeground(Color.RED);
		this.errorLabel.setVisible(false);

		this.manningsGraph = this.createGraph();
		this.manningsGraphContainer = this.createGraphContainer(this.manningsGraph);
		this.manningsGraphController = new GraphController(this.manningsGraphContainer);
		this.manningsGraphEditDialog = new GraphEditDialog(this.parent, new GraphEditScreen(this.manningsGraphContainer));
		this.manningsGraphEditDialog.addPropertyChangeListener(this.manningsGraphController);

		this.outputPrecision = new Wrapper<>(ResultScreen.DEFAULT_DISPLAYED_SCALE);
		this.outputPrecisionController = new SpinnerController<>(this.outputPrecision);

		this.workerDialog = new SwingWorkerDialog(this.parent, "Calculate", "Calculating Water Level...");

		this.add(this.createSidePanel(), BorderLayout.WEST);
		this.add(this.manningsGraphContainer, BorderLayout.CENTER);

		this.updateModelConstants();
		this.refreshGraph();
	}

	private void refreshOutput()
	{
		this.refreshOutputLabels();
		this.refreshErrorMessage();
	}

	private void refreshGraph()
	{
		this.function.update();
		this.manningsGraph.repaint();
	}

	private void refreshOutputLabels()
	{
		if (this.level.value != null && this.a.value != null && this.v.value != null)
		{
			this.levelLabel.setText(this.level.value.toString());
			this.aLabel.setText(this.a.value.toString());
			this.vLabel.setText(this.v.value.toString());
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
		switch (this.error)
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
		inputPanel.add(this.integerSpinnerPanel("Output Scale: ", this.outputPrecisionController, ResultScreen.MIN_DISPLAYED_SCALE, ResultScreen.MAX_DISPLAYED_SCALE, 1));
		inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(inputPanel);

		JButton calculate = new JButton(this.calculateAction());
		calculate.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(calculate);

		JPanel outputPanel = new JPanel();
		outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));
		outputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		outputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		outputPanel.add(this.componentPanel(this.errorLabel));
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
						ResultScreen.this.updateModelConstants();
						ResultScreen.this.refreshGraph();
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

	private JPanel integerSpinnerPanel(String label, SpinnerController<Integer> controller, int min, int max, int step)
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

	private Action calculateAction()
	{
		return new AbstractAction("Calculate")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ResultScreen.this.updateModelConstants();
				ResultScreen.this.calcOutputValues(ResultScreen.this.q.value.doubleValue());
				ResultScreen.this.refreshOutput();
			}
		};
	}

	private void updateModelConstants()
	{
		this.model.setN(this.n.value);
		this.model.setS(this.s.value);
	}

	private void calcOutputValues(double q)
	{
		class CalcWorker extends SwingWorker<Double, Object>
		{
			private ManningsModel model;
			private double discharge;
			private int displayScale;
	
			public CalcWorker(ManningsModel model, double discharge, int displayScale)
			{
				super();
				this.model = new ManningsModel(model);
				this.discharge = discharge;
				this.displayScale = displayScale;
			}

			@Override
			protected Double doInBackground() throws Exception
			{
				if (this.model.areConstantsSet() && !this.model.dischargeUnderflow(this.discharge) && this.model.canUseData())
				{
					return this.model.calcWaterLevel(this.discharge, this.displayScale, () -> !this.isCancelled());
				}
				else
				{
					return null;
				}
			}

			@Override
			protected void done()
			{
				if (!this.isCancelled())
				{
					Double level = null;
					try
					{
						level = this.get();
					}
					catch (InterruptedException | ExecutionException e)
					{
						e.printStackTrace();
					}
					ResultScreen.this.updateWaterLevel(this.discharge, level);

					if (!this.model.areConstantsSet())
					{
						ResultScreen.this.waterLevelError(ModelError.CONSTANTS_NOT_SET);
					}
					else if (this.model.dischargeUnderflow(this.discharge))
					{
						ResultScreen.this.waterLevelError(ModelError.DISCHARGE_UNDERFLOW);
					}
					else if (!this.model.canUseData())
					{
						ResultScreen.this.waterLevelError(ModelError.NOT_ENOUGH_DATA);
					}
					else
					{
						ResultScreen.this.waterLevelError(ModelError.NONE);
					}
				}
			}
		}
		
		CalcWorker worker = new CalcWorker(this.model, q, this.outputPrecision.value);
		this.workerDialog.open(worker);
	}

	private void updateWaterLevel(double discharge, Double level)
	{
		if (level == null || (discharge != 0 && this.model.calcArea(level) == 0))
		{
			this.level.value = null;
			this.a.value = null;
			this.v.value = null;
			return;
		}
		this.level.value = new BigDecimal(level).setScale(this.outputPrecision.value, RoundingMode.HALF_UP);
		this.a.value = new BigDecimal(this.model.calcArea(level)).setScale(this.outputPrecision.value, RoundingMode.HALF_UP);
		this.v.value = new BigDecimal(this.model.calcVelocity(discharge, level)).setScale(this.outputPrecision.value, RoundingMode.HALF_UP);
	}

	private void waterLevelError(ModelError error)
	{
		this.error = error;
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
