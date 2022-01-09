package ui;

import java.awt.Frame;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.function.Consumer;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public abstract class EditDialog extends JDialog implements PropertyChangeListener
{
	private JOptionPane pane;

	public EditDialog(Frame parentWindow, String title, JPanel screen)
	{
		super(parentWindow, title, true);
		
		this.pane = new JOptionPane(screen, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		this.setContentPane(this.pane);
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		pane.addPropertyChangeListener(JOptionPane.VALUE_PROPERTY, this);
	}

	protected abstract void prepareScreen();

	public void open(Consumer<EditDialog> consumer)
	{
		this.prepareScreen();
		this.pack();
		this.setLocationRelativeTo(this.getOwner());
		if (consumer != null)
		{
			consumer.accept(this);
		}
		this.setVisible(true);
	}

	public void open()
	{
		this.open(null);
	}

	// Since closing with the x button does not result in the calling of this method, this method should only hide the dialog
	public void close()
	{
		this.setVisible(false);
	}

	protected abstract boolean canOkClose();
	protected abstract void okCloseActions();

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
				if (this.canOkClose())
				{
					this.okCloseActions();
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
}
