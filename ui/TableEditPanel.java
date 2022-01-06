package ui;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

import data.DataPrecision;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

/**
 * Contains components for editing table as well as updating data connected to table.
**/

public class TableEditPanel extends JPanel implements ActionListener
{
	private JTable table;
	private DataPrecision precision;
	
	public TableEditPanel(JTable table, DataPrecision precision)
	{
		this.table = table;
		this.precision = precision;
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(this.createEditButton());
	}

	public JButton createEditButton()
	{
		JButton button = new JButton("Edit Cell");
		button.setActionCommand("edit");
		button.addActionListener(this);
		return button;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("edit"))
		{
			this.edit();
		}
	}

	private void edit()
	{
		if (this.table.getSelectedRow() != -1 && this.table.getSelectedColumn() != -1)
		{
			String input = JOptionPane.showInputDialog(this, "Enter new value: ");
			if (input != null)
			{
				try
				{
					BigDecimal value = this.table.getSelectedColumn() == 0 ? this.precision.fitPrecisionX(new BigDecimal(input)) : this.precision.fitPrecisionY(new BigDecimal(input));
					this.table.setValueAt(value, this.table.getSelectedRow(), this.table.getSelectedColumn());
					this.getRootPane().repaint();
				}
				catch (NumberFormatException e) {}
			}
		}
	}
}
