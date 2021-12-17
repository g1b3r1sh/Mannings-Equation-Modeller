package ui;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

import graphs.DiscreteData;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

/**
 * Contains components for editing table as well as updating data connected to table.
**/

public class TableEditPanel extends JPanel implements ActionListener
{
	private JTable table;
	private DiscreteData<BigDecimal, BigDecimal> data;
	
	public TableEditPanel(JTable table, DiscreteData<BigDecimal, BigDecimal> data)
	{
		this.table = table;
		this.data = data;
		
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
					BigDecimal value = new BigDecimal(input).setScale(this.table.getSelectedColumn() == 0 ? this.data.getPrecisionX() : this.data.getPrecisionY());
					this.table.setValueAt(value, this.table.getSelectedRow(), this.table.getSelectedColumn());
					this.getRootPane().repaint();
				}
				catch (NumberFormatException e) {}
			}
		}
	}
}
