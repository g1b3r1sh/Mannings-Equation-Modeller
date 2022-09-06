package spinner;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SpinnerController<T> implements ChangeListener
{
	private ChangeListener listener = null;
	private JSpinner spinner;
	private Supplier<T> get;
	private Consumer<T> set;

	public SpinnerController(Supplier<T> get, Consumer<T> set)
	{
		this.get = get;
		this.set = set;
	}

	public T getValue()
	{
		return this.get.get();
	}

	public void setValue(T value)
	{
		this.set.accept(value);
		this.spinner.setValue(value);
	}

	public void setSpinner(JSpinner spinner)
	{
		if (this.spinner != null)
		{
			this.spinner.removeChangeListener(this);
		}
		this.spinner = spinner;
		if (this.spinner != null)
		{
			this.spinner.addChangeListener(this);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void stateChanged(ChangeEvent e)
	{
		if (e.getSource() == this.spinner)
		{
			this.set.accept((T) this.spinner.getValue());
			this.notifyListener();
		}
	}

	public void setListener(ChangeListener listener)
	{
		this.listener = listener;
	}

	private void notifyListener()
	{
		if (this.listener != null)
		{
			this.listener.stateChanged(new ChangeEvent(this));
		}
	}
}
