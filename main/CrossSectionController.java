package main;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import javax.swing.AbstractAction;
import javax.swing.Action;

import data.DataScale;
import data.functions.DiscreteData;
import main.dialogs.DataEditDialog;
import main.input.InputScreen;
import spinner.ScaleSpinnerModel;

public class CrossSectionController
{
	DataEditDialog editDialog;
	InputScreen inputScreen;
	CrossSectionModel model;

	public CrossSectionController(CrossSectionModel model, MainWindow window)
	{
		this.editDialog = window.getInputScreen().getEditDialog();
		this.editDialog.addUpdateAction(this.getUpdateAction());
		this.inputScreen = window.getInputScreen();
		this.model = model;
	}

	public Action getUpdateAction()
	{
		return new AbstractAction("Update Data")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (e.getSource() instanceof CrossSectionModel)
				{	
					CrossSectionController.this.copyData((CrossSectionModel) e.getSource());
				}
			}
		};
	}

	private void matchData(DiscreteData<BigDecimal, BigDecimal> newData)
	{
		this.model.getMutableData().load(newData);
	}

	private void matchScale(DataScale newScale)
	{
		this.model.getScale().setX(newScale.getX());
		this.model.getScale().setY(newScale.getY());
	}

	private void copyData(CrossSectionModel model)
	{
		CrossSectionController.this.matchData(model.getData());
		CrossSectionController.this.matchScale(model.getScale());
		((ScaleSpinnerModel) this.inputScreen.getWaterLevelSpinner().getModel()).setScale(this.model.getScale().getY()); // TODO: Delete after adding listeners for DataScale in inputscreen, then, decouple from InputScreen
	}
}
