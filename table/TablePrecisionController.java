package table;

import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TablePrecisionController implements ChangeListener
{
	EditableDiscreteDataModel model;
	SpinnerNumberModel spinnerX = null;
	SpinnerNumberModel spinnerY = null;
	
	public TablePrecisionController(EditableDiscreteDataModel model)
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
		this.spinnerX.setValue(this.model.getPrecision().getX());
		this.spinnerY.setValue(this.model.getPrecision().getY());
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		if (e.getSource() == this.spinnerX)
		{
			this.model.getPrecision().setX((Integer) this.spinnerX.getValue());
			this.model.updatePrecisionX();
		}
		else if (e.getSource() == this.spinnerY)
		{
			this.model.getPrecision().setY((Integer) this.spinnerY.getValue());
			this.model.updatePrecisionY();
		}
	}}