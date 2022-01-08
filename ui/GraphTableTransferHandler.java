package ui;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import java.awt.datatransfer.Transferable;

public class GraphTableTransferHandler extends JTableTransferHandler
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
			if (table.getModel() instanceof GraphTableModel)
			{
				GraphTableModel model = (GraphTableModel) table.getModel();
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
}
