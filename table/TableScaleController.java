package table;

import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TableScaleController implements ChangeListener
{
	EditableDiscreteDataModel model;
	SpinnerNumberModel spinnerX = null;
	SpinnerNumberModel spinnerY = null;
	
	public TableScaleController(EditableDiscreteDataModel model)
	{
		this.model = model;
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

	public void refreshSpinnerValues()
	{
		this.spinnerX.setValue(this.model.getScale().getX());
		this.spinnerY.setValue(this.model.getScale().getY());
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		if (e.getSource() == this.spinnerX)
		{
			this.model.getScale().setX((Integer) this.spinnerX.getValue());
			this.model.updateScaleX();
		}
		else if (e.getSource() == this.spinnerY)
		{
			this.model.getScale().setY((Integer) this.spinnerY.getValue());
			this.model.updateScaleY();
		}
	}}
