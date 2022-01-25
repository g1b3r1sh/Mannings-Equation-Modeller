package table;

import javax.swing.AbstractAction;
import javax.swing.JTable;

import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class DiscreteDataTableController
{
	private JTable table;
	private DiscreteDataTableModel<?, ?> tableModel;
	private HashMap<Class<? extends ControllerAction>, ControllerAction> actions;

	public DiscreteDataTableController(JTable table, DiscreteDataTableModel<?, ?> tableModel)
	{
		this.table = table;
		this.tableModel = tableModel;
		this.actions = new HashMap<>();
	}

	@SuppressWarnings("unchecked")
	private <T extends ControllerAction> T getAction(Class<T> c)
	{
		if (this.actions.get(c) == null)
		{
			try
			{
				this.actions.put(c, c.getDeclaredConstructor(DiscreteDataTableController.class).newInstance(this));
			}
			catch (IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException | NoSuchMethodException e)
			{
				e.printStackTrace();
			}
		}
		return (T) this.actions.get(c);
	}

	public InsertAction getInsertAction()
	{
		return this.getAction(InsertAction.class);
	}

	public InsertLastAction getInsertLastAction()
	{
		return this.getAction(InsertLastAction.class);
	}

	public DeleteRowsAction getDeleteRowsAction()
	{
		return this.getAction(DeleteRowsAction.class);
	}

	public ClearSelectedAction getClearSelectedAction()
	{
		return this.getAction(ClearSelectedAction.class);
	}

	public NewTableAction getNewTableAction()
	{
		return this.getAction(NewTableAction.class);
	}
	
	public PrintSelectedAction getPrintSelectedAction()
	{
		return this.getAction(PrintSelectedAction.class);
	}

	public abstract class ControllerAction extends AbstractAction
	{
		private DiscreteDataTableController controller;

		public ControllerAction(String name)
		{
			super(name);
			this.controller = DiscreteDataTableController.this;
		}

		protected DiscreteDataTableController getController()
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
				this.getController().tableModel.deleteRows(this.getController().table.getSelectedRow(), rowCount);
			}
		}
	}

	public class ClearSelectedAction extends ControllerAction
	{
		public ClearSelectedAction()
		{
			super("Clear Selected");
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			this.getController().tableModel.clear(this.getController().table.getSelectedRows(), this.getController().table.getSelectedColumns());
		}
	}

	public class NewTableAction extends ControllerAction
	{
		public NewTableAction()
		{
			super("New Table");
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			this.getController().tableModel.newTable();
		}
	}

	public class PrintSelectedAction extends ControllerAction
	{
		public PrintSelectedAction()
		{
			super("Print Selected");
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
