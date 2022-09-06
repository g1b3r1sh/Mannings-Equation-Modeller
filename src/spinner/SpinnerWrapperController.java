package spinner;

import javax.swing.event.ChangeListener;

import utility.Wrapper;

public class SpinnerWrapperController<T> extends SpinnerController<T>
{
	public SpinnerWrapperController(Wrapper<T> value)
	{
		super(value::get, value::set);
	}

	public SpinnerWrapperController(Wrapper<T> value, ChangeListener listener)
	{
		this(value);
		this.setListener(listener);
	}
}
