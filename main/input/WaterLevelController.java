package main.input;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import data.DataScale;
import hydraulics.WaterLevelCalculator;
import spinner.ScaleSpinnerModel;

public class WaterLevelController implements PropertyChangeListener, ChangeListener
{
	private WaterLevelCalculator<?, ?> calculator;
	private DataScale scale;
	private ScaleSpinnerModel spinnerModel;

	public WaterLevelController(WaterLevelCalculator<?, ?> calculator)
	{
		this.calculator = calculator;
	}

	public void addScale(DataScale scale)
	{
		if (this.scale != null)
		{
			this.scale.removePropertyChangeListener(this);
		}
		this.scale = scale;
		this.scale.addPropertyChangeListener(this);
	}

	public void addSpinnerModel(ScaleSpinnerModel spinnerModel)
	{
		if (this.spinnerModel != null)
		{
			this.spinnerModel.removeChangeListener(this);
		}
		this.spinnerModel = spinnerModel;
		this.spinnerModel.addChangeListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		if (e.getSource() == this.spinnerModel)
		{
			this.calculator.setWaterLevel(this.spinnerModel.getNumber());
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		if (evt.getSource() == this.scale && "y".equals(evt.getPropertyName()))
		{
			this.spinnerModel.setScale(this.scale.getY());
		}
	}
}
