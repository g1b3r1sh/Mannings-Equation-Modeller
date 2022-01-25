package main;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

import data.DataPrecision;
import main.dialogs.DataEditDialog;
import main.input.InputScreen;
import spinner.PrecisionSpinnerModel;
import table.EditableDiscreteDataModel;
import utility.Pair;

public class CrossSectionController implements PropertyChangeListener
{
	DataEditDialog editDialog;
	InputScreen inputScreen;
	CrossSectionModel model;

	public CrossSectionController(CrossSectionModel model, MainWindow window)
	{
		this.editDialog = window.getInputScreen().getEditDialog();
		this.editDialog.addPropertyChangeListener(this);
		this.inputScreen = window.getInputScreen();
		this.model = model;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		if (evt.getSource() == this.editDialog)
		{
			if ("update".equals(evt.getPropertyName()))
			{
				EditableDiscreteDataModel newData = (EditableDiscreteDataModel) evt.getNewValue();

				this.matchData(newData);
				this.matchPrecision(newData.getPrecision());

				this.dataUpdated();
			}
		}
	}

	private void matchData(EditableDiscreteDataModel newData)
	{

		this.model.getData().clear();
		for (Pair<BigDecimal, BigDecimal> row : newData.getData())
		{
			if (row.first != null && row.second != null)
			{
				this.model.getData().set(row.first, row.second);
			}
		}
	}

	private void matchPrecision(DataPrecision newPrecision)
	{
		this.model.getPrecision().setX(newPrecision.getX());
		this.model.getPrecision().setY(newPrecision.getY());
	}

	private void dataUpdated()
	{
		this.inputScreen.getTableModel().refresh();
		this.inputScreen.getGraph().repaint();
		((PrecisionSpinnerModel) this.inputScreen.getWaterLevelSpinner().getModel()).setPrecision(this.model.getPrecision().getY());
	}
}
