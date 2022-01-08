package ui;

import javax.swing.AbstractAction;
import javax.swing.JTable;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class DiscreteDataTableController
{
	private JTable table;
	private DiscreteDataTableModel<?, ?> tableModel;

	public DiscreteDataTableController(JTable table, DiscreteDataTableModel<?, ?> tableModel)
	{
		this.table = table;
		this.tableModel = tableModel;
	}

	public abstract class ControllerAction extends AbstractAction
	{
		private DiscreteDataTableController controller;

		public ControllerAction(String name)
		{
			super(name);
			this.controller = DiscreteDataTableController.this;
		}

		public DiscreteDataTableController getController()
		{
			return this.controller;
		}
	}

	public class InsertAction extends ControllerAction
	{
		public InsertAction()
		{
			super("Insert Row");
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			this.getController().tableModel.insertRow(this.getController().table.getSelectedRow());
		}
	}

	public class InsertLastAction extends ControllerAction
	{
		public InsertLastAction()
		{
			super("Add Row to Last");
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			this.getController().tableModel.insertRowLast();
		}
	}

	public class DeleteRowsAction extends ControllerAction
	{
		public DeleteRowsAction()
		{
			super("Delete Selected Rows");
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			int rowCount = this.getController().table.getSelectedRowCount();
			if (rowCount > 0)
			{
				this.getController().tableModel.deleteRows(this.getController().table.getSelectedRows()[0], rowCount);
			}
		}
	}

	public class ClearCellsAction extends ControllerAction
	{
		public ClearCellsAction()
		{
			super("Clear Cells");
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			this.getController().tableModel.clear(this.getController().table.getSelectedRows(), this.getController().table.getSelectedColumns());
		}
	}

	public class PrintSelectedAction extends ControllerAction
	{
		public PrintSelectedAction()
		{
			super("Print Selected");
			putValue(AbstractAction.ACCELERATOR_KEY, KeyEvent.VK_K);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			System.out.print("[");
			for (int row : this.getController().table.getSelectedRows())
			{
				for (int column : this.getController().table.getSelectedColumns())
				{
					System.out.printf("%s, ", this.getController().tableModel.getValueAt(row, column).toString());
				}
			}
			System.out.printf("]\n");
		}
	}
}
