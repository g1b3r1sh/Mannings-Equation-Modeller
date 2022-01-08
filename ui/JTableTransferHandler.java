package ui;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.table.TableModel;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

public class JTableTransferHandler extends TransferHandler
{
	public JTableTransferHandler()
	{
		super();
	}

	@Override
	public int getSourceActions(JComponent c)
	{
		return TransferHandler.COPY;
	}

	@Override
	protected Transferable createTransferable(JComponent c)
	{
		if (c instanceof JTable)
		{
			JTable table = (JTable) c;
			TableModel model = table.getModel();
			if (table.getSelectedRowCount() > 0 && table.getSelectedColumnCount() > 0)
			{
				StringBuilder s = new StringBuilder();
				for (int y = 0; y < table.getSelectedRowCount(); y++)
				{
					for (int x = 0; x < table.getSelectedColumnCount(); x++)
					{
						Object o = model.getValueAt(table.getSelectedRows()[y], table.getSelectedColumns()[x]);
						if (o != null)
						{
							s.append(o.toString());
						}
						if (x != table.getSelectedColumnCount() - 1)
						{
							s.append("\t");
						}
					}
					s.append("\n");
				}
				return new StringSelection(s.toString());
			}
		}
		return null;
	}
}
