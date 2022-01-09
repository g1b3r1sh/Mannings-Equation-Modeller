package ui;

import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SpinnerController<T> implements ChangeListener
{
	private JSpinner spinner;
	private Wrapper<T> value;
	private ChangeListener listener;

	public SpinnerController(Wrapper<T> value, ChangeListener listener)
	{
		this.value = value;
		this.listener = listener;
	}

	public void setValue(T value)
	{
		this.value.value = value;
		this.spinner.setValue(this.value.value);
	}

	public void addSpinner(JSpinner spinner)
	{
		if (this.spinner != null)
		{
			this.spinner.removeChangeListener(this);
		}
		this.spinner = spinner;
		this.spinner.addChangeListener(this);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void stateChanged(ChangeEvent e)
	{
		if (e.getSource() == this.spinner)
		{
			this.value.value = (T) this.spinner.getValue();
			this.notifyListener();
		}
	}

	private void notifyListener()
	{
		if (this.listener != null)
		{
			this.listener.stateChanged(new ChangeEvent(this));
		}
	}
}
