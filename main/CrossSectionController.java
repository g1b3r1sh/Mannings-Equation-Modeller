package main;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

import data.DataScale;
import main.dialogs.DataEditDialog;
import main.input.InputScreen;
import spinner.ScaleSpinnerModel;
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
				this.matchScale(newData.getScale());

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

	private void matchScale(DataScale newScale)
	{
		this.model.getScale().setX(newScale.getX());
		this.model.getScale().setY(newScale.getY());
	}

	private void dataUpdated()
	{
		this.inputScreen.refreshTableModel();
		this.inputScreen.repaintGraph();
		((ScaleSpinnerModel) this.inputScreen.getWaterLevelSpinner().getModel()).setScale(this.model.getScale().getY());
	}
}
