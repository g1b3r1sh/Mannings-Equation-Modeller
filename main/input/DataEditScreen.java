package main.input;

import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.JTextComponent;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import data.DataPrecision;
import data.MapDiscreteData;
import ui.DiscreteDataTableController;
import ui.TablePrecisionController;
import ui.GraphTableModel;
import ui.GraphTableTransferHandler;

public class DataEditScreen extends JPanel
{
	private GraphTableModel tableModel;
	private JTable table;
	private TablePrecisionController precisionController;
	private DiscreteDataTableController tableController;

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

	public GraphTableModel getModel()
	{
		return this.tableModel;
	}

	public DiscreteDataTableController getController()
	{
		return this.tableController;
	}

	public void refresh()
	{
		// Update screen to be consistent with current values
		this.tableModel.refresh();
		this.table.clearSelection();
		this.precisionController.refreshSpinnerValues();
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
		table.getTableHeader().setReorderingAllowed(false);
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
		this.tableController = new DiscreteDataTableController(table, this.tableModel);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(this.createTableControls(this.tableController));
		panel.add(this.createPrecisionControls(this.tableController));
		panel.add(Box.createVerticalGlue());

		this.setupShortcuts(this.tableController);
		return panel;
	}

	private JPanel createTableControls(DiscreteDataTableController controller)
	{
		JPanel tableControls = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tableControls.add(new JButton(controller.getInsertAction()));
		tableControls.add(new JButton(controller.getInsertLastAction()));
		tableControls.add(new JButton(controller.getDeleteRowsAction()));
		tableControls.add(new JButton(controller.getClearSelectedAction()));
		tableControls.add(new JButton(controller.getNewTableAction()));
		return tableControls;
	}

	private void setupShortcuts(DiscreteDataTableController controller)
	{
		this.addShortcut(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK), controller.getInsertAction());
		this.addShortcut(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), controller.getInsertLastAction());
		this.addShortcut(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), controller.getNewTableAction());
		this.addShortcut(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), controller.getClearSelectedAction());
		
		// Allow use of document editing commands by using a custom transfer handler
		this.table.setTransferHandler(new GraphTableTransferHandler());
		// Disable windows key so that it's possible to paste from windows clipboard history using Win+V
		this.addShortcut(KeyStroke.getKeyStroke(KeyEvent.VK_WINDOWS, 0), new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				return;
			}
		});
	}

	private JPanel createPrecisionControls(DiscreteDataTableController controller)
	{
		// Create and link spinner models to controller
		SpinnerNumberModel modelX = new SpinnerNumberModel(this.tableModel.getPrecision().getX(), null, null, 1);
		SpinnerNumberModel modelY = new SpinnerNumberModel(this.tableModel.getPrecision().getY(), null, null, 1);
		this.precisionController = new TablePrecisionController(this.tableModel);
		this.precisionController.setSpinnerX(modelX);
		this.precisionController.setSpinnerY(modelY);

		// Construct panel containing controls
		JPanel precisionControls = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JPanel precisionXControls = new JPanel();
		JPanel precisionYControls = new JPanel();
		precisionXControls.setLayout(new BoxLayout(precisionXControls, BoxLayout.X_AXIS));
		precisionYControls.setLayout(new BoxLayout(precisionYControls, BoxLayout.X_AXIS));
		precisionXControls.add(new JLabel("Precision x: "));
		precisionXControls.add(this.createPrecisionSpinner(modelX));
		precisionYControls.add(new JLabel("Precision y: "));
		precisionYControls.add(this.createPrecisionSpinner(modelY));

		precisionControls.add(precisionXControls);
		precisionControls.add(precisionYControls);
		return precisionControls;
	}

	private JSpinner createPrecisionSpinner(SpinnerModel model)
	{
		JSpinner spinner = new JSpinner(model);
		spinner.setPreferredSize(new Dimension(50, spinner.getPreferredSize().height));
		return spinner;
	}

	private void addShortcut(KeyStroke keyStroke, Action action)
	{
		this.table.getInputMap().put(keyStroke, action);
		this.table.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, action);
		this.table.getActionMap().put(action, action);
	}
}
