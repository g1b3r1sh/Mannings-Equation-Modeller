package table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import data.DataScale;

public class ScaleSpinnerController implements ChangeListener, PropertyChangeListener
{
	private DataScale scale;
	private SpinnerNumberModel spinnerX = null;
	private SpinnerNumberModel spinnerY = null;

	public ScaleSpinnerController(DataScale scale)
	{
		this.scale = scale;
		this.scale.addPropertyChangeListener(this);
	}

	public void setSpinnerX(SpinnerNumberModel spinnerX)
	{
		if (this.spinnerX != null)
		{
			this.spinnerX.removeChangeListener(this);
		}
		this.spinnerX = spinnerX;
		this.spinnerX.addChangeListener(this);
	}

	public void setSpinnerY(SpinnerNumberModel spinnerY)
	{
		if (this.spinnerY != null)
		{
			this.spinnerY.removeChangeListener(this);
		}
		this.spinnerY = spinnerY;
		this.spinnerY.addChangeListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		if (e.getSource() == this.spinnerX)
		{
			this.scale.setX((Integer) this.spinnerX.getValue());
		}
		else if (e.getSource() == this.spinnerY)
		{
			this.scale.setY((Integer) this.spinnerY.getValue());
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		if (evt.getSource() == this.scale)
		{
			if ("x".equals(evt.getPropertyName()))
			{
				this.spinnerX.setValue(this.scale.getX());
			}
			else if ("y".equals(evt.getPropertyName()))
			{
				this.spinnerY.setValue(this.scale.getY());
			}
		}
	}
}
