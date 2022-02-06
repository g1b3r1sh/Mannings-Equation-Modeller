package spinner;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import data.Range;

public class RangeSpinnerController implements ChangeListener, PropertyChangeListener
{
	private Range range;
	private JSpinner lowerSpinner;
	private JSpinner upperSpinner;

	public RangeSpinnerController(Range range)
	{
		this.range = range;
		this.range.addPropertyChangeListener​(this);
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
		}
		else if (e.getSource() == this.upperSpinner)
		{
			int newValue = (int) this.upperSpinner.getValue();
			if (newValue <= (int) this.lowerSpinner.getValue())
			{
				this.lowerSpinner.setValue(newValue - 1);
			}
			this.range.setUpper(newValue);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		if (evt.getSource() == this.range)
		{
			if ("lower".equals(evt.getPropertyName()))
			{
				this.lowerSpinner.setValue(evt.getNewValue());
			}
			else if ("upper".equals(evt.getPropertyName()))
			{
				this.upperSpinner.setValue(evt.getNewValue());
			}
		}
	}
}
