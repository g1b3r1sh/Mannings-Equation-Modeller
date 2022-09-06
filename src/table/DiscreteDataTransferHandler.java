package table;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.math.BigDecimal;

public class DiscreteDataTransferHandler extends JTableTransferHandler
{
	@Override
	public int getSourceActions(JComponent c)
	{
		return TransferHandler.COPY_OR_MOVE;
	}

	@Override
	protected void exportDone(JComponent source, Transferable data, int action)
	{
		if (action == TransferHandler.MOVE && source instanceof JTable)
		{
			JTable table = (JTable) source;
			if (table.getModel() instanceof EditableDiscreteDataModel)
			{
				EditableDiscreteDataModel model = (EditableDiscreteDataModel) table.getModel();
				if (table.getSelectedColumnCount() == 1)
				{
					for (int row : table.getSelectedRows())
					{
						model.setValueAt(null, row, table.getSelectedColumn());
					}
				}
				else
				{
					model.deleteRows(table.getSelectedRow(), table.getSelectedRowCount());
				}
			}	
		}
	}

	@Override
	public boolean importData(JComponent comp, Transferable t)
	{
		if (this.canImport(comp, t.getTransferDataFlavors()))
		{
			JTable table = (JTable) comp;
			String data;
			try
			{
				data = (String) t.getTransferData(DataFlavor.stringFlavor);
			}
			catch (IOException | UnsupportedFlavorException e)
			{
				e.printStackTrace();
				return false;
			}
			if (table.getModel() instanceof EditableDiscreteDataModel)
			{
				EditableDiscreteDataModel model = (EditableDiscreteDataModel) table.getModel();
				if (this.canInsert(data))
				{
					String[] rows = this.splitRows(data);

					int numRows = rows.length;
					int numCols = 0;
					for (String row : rows)
					{
						if (this.splitValues(row).length > numCols)
						{
							numCols = this.splitValues(row).length;
						}
					}

					BigDecimal[][] decimalData = new BigDecimal[numRows][numCols];
					for (int y = 0; y < numRows; y++)
					{
						String[] values = this.splitValues(rows[y]);
						for (int x = 0; x < values.length; x++)
						{
							if (!"".equals(values[x]))
							{
								decimalData[y][x] = new BigDecimal(values[x]);
							}
						}
					}

					model.pasteData(decimalData, table.getSelectedRow(), table.getSelectedColumn());
				}
			}
		}
		return false;
	}
	
	// Returns true if comp is an instance of JTable, cells are selected in it, and the data is in string format
	@Override
	public boolean canImport(JComponent comp, DataFlavor[] transferFlavors)
	{
		if (comp instanceof JTable)
		{
			JTable table = (JTable) comp;
			if (table.getSelectedRowCount() > 0 && table.getSelectedColumnCount() > 0)
			{
				for (DataFlavor flavor : transferFlavors)
				{
					if (flavor == DataFlavor.stringFlavor)
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	// Returns whether string is in spreadsheet format and the values can be converted into numbers
	private boolean canInsert(String s)
	{
		for (String row : this.splitRows(s))
		{
			for (String value : this.splitValues(row))
			{
				if (!"".equals(value))
				{
					try
					{
						new BigDecimal(value);
					}
					catch (NumberFormatException e)
					{
						return false;
					}
				}
			}
		}
		return true;
	}

	private String[] splitRows(String data)
	{
		return data.split("\n");
	}

	private String[] splitValues(String row)
	{
		return row.split("\t", -1);
	}
}
