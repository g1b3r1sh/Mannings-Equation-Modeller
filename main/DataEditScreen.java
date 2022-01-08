package main;

import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.JTextComponent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import data.DataPrecision;
import data.MapDiscreteData;
import ui.DiscreteDataTableController;
import ui.GraphTableModel;

public class DataEditScreen extends JPanel
{
	private GraphTableModel tableModel;
	private JTable table;

	public DataEditScreen(MapDiscreteData<BigDecimal, BigDecimal> data, DataPrecision precision, String xLabel, String yLabel)
	{
		super(new BorderLayout());

		this.tableModel = new GraphTableModel(data, precision, xLabel, yLabel)
		{
			@Override
			public boolean isCellEditable(int row, int col)
			{
				return true;
			}
		};

		this.table = this.createTable(data, precision, xLabel, yLabel);
		this.add(this.initTablePane(table), BorderLayout.WEST);
		this.add(this.createControlPanel(table), BorderLayout.CENTER);
	}

	public void refresh()
	{
		// Update screen to be consistent with current values
		this.tableModel.refresh();
	}

	private JTable createTable(MapDiscreteData<BigDecimal, BigDecimal> data, DataPrecision precision, String xLabel, String yLabel)
	{
		JTable table = new JTable(this.tableModel)
		{
			@Override
			public boolean editCellAt(int row, int column, EventObject e)
			{
				boolean result = super.editCellAt(row, column, e);
				Component editor = this.getEditorComponent();
				if (editor != null)
				{
					((JTextComponent) editor).selectAll();
				}
				return result;
			}
		};
		table.setCellSelectionEnabled(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		return table;
	}
	
	private JScrollPane initTablePane(JTable table)
	{
		JScrollPane panel = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// Default width: 200
		panel.setPreferredSize(new Dimension(200, 500));
		return panel;
	}

	private JPanel createControlPanel(JTable table)
	{
		DiscreteDataTableController controller = new DiscreteDataTableController(table, this.tableModel);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(this.createTableControls(controller));
		panel.add(this.createPrecisionControls(controller));
		panel.add(Box.createVerticalGlue());
		return panel;
	}

	private JPanel createTableControls(DiscreteDataTableController controller)
	{
		JPanel tableControls = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tableControls.add(new JButton(controller.new InsertAction()));
		tableControls.add(new JButton(controller.new InsertAfterAction()));
		tableControls.add(new JButton(controller.new DeleteRowsAction()));
		tableControls.add(new JButton(controller.new ClearCellAction()));
		return tableControls;
	}

	private JPanel createPrecisionControls(DiscreteDataTableController controller)
	{
		JPanel precisionControls = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JPanel precisionXControls = new JPanel();
		JPanel precisionYControls = new JPanel();
		precisionXControls.setLayout(new BoxLayout(precisionXControls, BoxLayout.X_AXIS));
		precisionYControls.setLayout(new BoxLayout(precisionYControls, BoxLayout.X_AXIS));
		precisionXControls.add(new JLabel("Precision x: "));
		precisionXControls.add(new JSpinner(new SpinnerNumberModel(this.tableModel.getPrecision().getX(), 0, null, 1)));
		precisionYControls.add(new JLabel("Precision y: "));
		precisionYControls.add(new JSpinner(new SpinnerNumberModel(this.tableModel.getPrecision().getY(), 0, null, 1)));

		precisionControls.add(precisionXControls);
		precisionControls.add(precisionYControls);
		return precisionControls;
	}
}
