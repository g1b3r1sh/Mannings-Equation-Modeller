package main.result;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;

import spinner.SpinnerController;
import spinner.SpinnerWrapperController;
import utility.Wrapper;

public class TableOutputPanel extends OutputPanel
{
	private static final BigDecimal DEFAULT_MIN_DISCHARGE = new BigDecimal(0);
	private static final BigDecimal DEFAULT_MAX_DISCHARGE = new BigDecimal(500);
	private static final int DEFAULT_NUM_DISCHARGE_ROWS = 5;
	private static final int MIN_NUM_DISCHARGE_ROWS = 0;
	
	private EnumMap<ResultScreenController.ModelError, JLabel> errorLabels;

	private Wrapper<BigDecimal> dischargeLower;
	private Wrapper<BigDecimal> dischargeUpper;
	private Wrapper<Integer> numDischargeRows;
	private SpinnerController<Integer> numDischargeRowsController;
	private ResultsTableModel tableModel;

	public TableOutputPanel(ResultScreen parent, ResultScreenController controller)
	{
		super(parent, controller);

		this.errorLabels = this.createErrorLabelsMap();
		this.dischargeLower = new Wrapper<>(TableOutputPanel.DEFAULT_MIN_DISCHARGE);
		this.dischargeUpper = new Wrapper<>(TableOutputPanel.DEFAULT_MAX_DISCHARGE);
		this.numDischargeRows = new Wrapper<>(TableOutputPanel.DEFAULT_NUM_DISCHARGE_ROWS);
		this.numDischargeRowsController = new SpinnerWrapperController<>(this.numDischargeRows);
		this.tableModel = new ResultsTableModel();

		this.addComponents();
	}

	/// Panel Methods

	private void addComponents()
	{
		this.add(this.inputPanel());
		this.add(ResultScreen.sidePadding());
		this.add(ResultScreen.sideButton(this.calculateAction()));
		this.add(ResultScreen.sidePadding());
		this.add(this.outputPanel());
	}

	private JPanel inputPanel()
	{
		JPanel panel = ResultScreen.mainSidePanel();
		
		panel.add(ResultScreen.integerSpinnerPanel("Output Rows: ", this.numDischargeRowsController, TableOutputPanel.MIN_NUM_DISCHARGE_ROWS, null, 1));
		panel.add(ResultScreen.sidePadding());
		panel.add(ResultScreen.numberEditPanel(this, "Discharge Min", () -> this.dischargeLower.get(), (discharge) -> this.dischargeLower.set(discharge)));
		panel.add(ResultScreen.sidePadding());
		panel.add(ResultScreen.numberEditPanel(this, "Discharge Max", () -> this.dischargeUpper.get(), (discharge) -> this.dischargeUpper.set(discharge)));
		panel.add(ResultScreen.sidePadding());

		return panel;
	}

	private JPanel outputPanel()
	{
		JPanel panel = ResultScreen.mainSidePanel();

		this.add(this.createErrorPanel());
		this.add(ResultScreen.sidePadding());
		this.add(this.createTablePane(this.createOutputTable()));

		return panel;
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
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		for (ResultScreenController.ModelError error : this.errorLabels.keySet())
		{
			panel.add(this.errorLabels.get(error));
		}
		return panel;
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

	/// Processing Methods

	private Action calculateAction()
	{
		return new AbstractAction("Calculate Table")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				TableOutputPanel.this.getController().updateModelConstants();
				ResultScreenController.Result[] results = TableOutputPanel.this.calcResults();
				if (results != null)
				{
					TableOutputPanel.this.processResults(results);
				}
			}
		};
	}

	private ResultScreenController.Result[] calcResults()
	{
		SwingWorker<ResultScreenController.Result[], ?> worker = this.getController().createResultsWorker
		(
			this.dischargeLower.value.doubleValue(),
			this.dischargeUpper.value.doubleValue(),
			this.numDischargeRows.value,
			this.getParentScreen().getPrecision()
		);
		this.getParentScreen().openWorker(worker);
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

	private void processResults(ResultScreenController.Result[] results)
	{
		this.tableModel.setData(results);
		this.showErrors(this.createErrorSet(results));
		this.getParentScreen().processResults(results);
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
}
