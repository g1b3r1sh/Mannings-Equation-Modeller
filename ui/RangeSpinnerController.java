package ui;

import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import graphs.Range;

public class RangeSpinnerController implements ChangeListener
{
	private Range range;
	private JSpinner lowerSpinner;
	private JSpinner upperSpinner;
	private ChangeListener listener;

	public RangeSpinnerController(Range range, ChangeListener listener)
	{
		this.range = range;
		this.listener = listener;
	}

	public Range getRange()
	{
		return this.range;
	}

	public void setLowerSpinner(JSpinner spinner)
	{
		if (this.lowerSpinner != null)
		{
			this.lowerSpinner.removeChangeListener(this);
		}
		this.lowerSpinner = spinner;
		this.lowerSpinner.addChangeListener(this);
	}

	public void setUpperSpinner(JSpinner spinner)
	{
		if (this.upperSpinner != null)
		{
			this.upperSpinner.removeChangeListener(this);
		}
		this.upperSpinner = spinner;
		this.upperSpinner.addChangeListener(this);
	}

	public void setRange(Range range)
	{
		this.range.copy(range);
		this.lowerSpinner.setValue(this.range.getLower());
		this.upperSpinner.setValue(this.range.getUpper());
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		if (e.getSource() == this.lowerSpinner)
		{
			int newValue = (int) this.lowerSpinner.getValue();
			if (newValue >= (int) this.upperSpinner.getValue())
			{
				this.upperSpinner.setValue(newValue + 1);
			}
			this.range.setLower(newValue);
			this.notifyListener();
		}
		else if (e.getSource() == this.upperSpinner)
		{
			int newValue = (int) this.upperSpinner.getValue();
			if (newValue <= (int) this.lowerSpinner.getValue())
			{
				this.lowerSpinner.setValue(newValue - 1);
			}
			this.range.setUpper(newValue);
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
