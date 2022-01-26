package main.dialogs;

import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public class SwingWorkerDialog extends JDialog implements PropertyChangeListener
{
	private static final String WORKER_PROPERTY_NAME = "state";
	private JOptionPane dialogPane;
	private JProgressBar bar;
	private SwingWorker<?, ?> worker;

	public SwingWorkerDialog(Frame parentWindow, String title, String message)
	{
		super(parentWindow, title, true);

		this.bar = this.createBar();
		this.dialogPane = new JOptionPane(this.createDialogPanel(message, this.bar), JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[] {"Cancel"});
		this.setContentPane(this.dialogPane);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.dialogPane.addPropertyChangeListener(JOptionPane.VALUE_PROPERTY, this);
		this.addWindowListener(this.createwindowListener());
	}

	private JPanel createDialogPanel(String message, JProgressBar bar)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel(message));
		panel.add(bar);
		return panel;
	}

	private JProgressBar createBar()
	{
		JProgressBar bar = new JProgressBar();
		bar.setIndeterminate(true);
		return bar;
	}

	// Returns null if worker was cancelled or if an exception occurs
	public <T> T runWorker(SwingWorker<T, ?> worker)
	{
		this.open(worker);
		if (worker.isCancelled())
		{
			return null;
		}
		try
		{
			return worker.get();
		}
		catch (InterruptedException | ExecutionException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public void open(SwingWorker<?, ?> worker)
	{
		if (!this.isVisible())
		{
			this.worker = worker;
			this.worker.getPropertyChangeSupport().addPropertyChangeListener(SwingWorkerDialog.WORKER_PROPERTY_NAME, this);
			this.worker.execute();

			this.bar.setIndeterminate(false);
			this.bar.setIndeterminate(true);

			this.pack();
			this.setLocationRelativeTo(this.getOwner());
			this.setVisible(true);
		}
	}

	public void close()
	{
		if (this.isVisible())
		{
			this.worker.removePropertyChangeListener(this);
			this.worker.cancel(true);
			this.dispose();
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent e)
	{
		if (this.isVisible())
		{
			if (e.getSource() == this.dialogPane && JOptionPane.VALUE_PROPERTY.equals(e.getPropertyName()))
			{
				Object value = e.getNewValue();
				if (value == JOptionPane.UNINITIALIZED_VALUE)
				{
					return;
				}

				this.close();
				this.dialogPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
			}
			else if (e.getSource() == this.worker && SwingWorkerDialog.WORKER_PROPERTY_NAME.equals(e.getPropertyName()))
			{
				if (e.getNewValue() == SwingWorker.StateValue.DONE)
				{
					this.close();
				}
			}
		}
	}

	private WindowListener createwindowListener()
	{
		return new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				SwingWorkerDialog.this.close();
			}
		};
	}
}
