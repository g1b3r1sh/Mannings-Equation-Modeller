package main;

import java.awt.Frame;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class DataEditDialog extends JDialog implements PropertyChangeListener
{
	private static final String TITLE = "Edit Data";

	private JOptionPane pane;
	private DataEditScreen screen;

	public DataEditDialog(Frame parentWindow, DataEditScreen screen)
	{
		super(parentWindow, DataEditDialog.TITLE, true);
		this.screen = screen;
		
		this.pane = new JOptionPane(screen, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		this.setContentPane(this.pane);
		this.setDefaultCloseOperation(DataEditDialog.HIDE_ON_CLOSE);
		pane.addPropertyChangeListener(JOptionPane.VALUE_PROPERTY, this);
	}

	public void open()
	{
		this.screen.refresh();
		this.pack();
		this.setLocationRelativeTo(this.getOwner());
		this.setVisible(true);
	}

	// Since closing with the x button does not result in the calling of this method, this method should only hide the dialog
	public void close()
	{
		this.setVisible(false);
	}

	@Override
	public void propertyChange(PropertyChangeEvent e)
	{
		if (this.isVisible() && e.getSource() == this.pane)
		{
			Object value = e.getNewValue();
			if (value == JOptionPane.UNINITIALIZED_VALUE)
			{
				return;
			}

			if ((Integer) value == JOptionPane.OK_OPTION)
			{
				// Validate input, if valid, call event for setting values and close
				if (this.okClose())
				{
					this.close();
				}
			}
			else if ((Integer) value == JOptionPane.CANCEL_OPTION)
			{
				this.close();
			}

			this.pane.setValue(JOptionPane.UNINITIALIZED_VALUE);
		}
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension((int) (this.getOwner().getWidth() * 0.9), (int) (this.getOwner().getHeight() * 0.9));
	}

	private boolean okClose()
	{
		if (this.screen.getModel().containsDuplicates())
		{
			JOptionPane.showMessageDialog(this, "Error: X column contains duplicates.", "Error: Duplicates", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (this.screen.getModel().containsEmpty())
		{
			int result = JOptionPane.showConfirmDialog(this, "Warning: Some cells are blank. They will not be included when updating the data. Continue?", "Warning: Blanks", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.NO_OPTION)
			{
				return false;
			}
		}
		return true;
	}
}
