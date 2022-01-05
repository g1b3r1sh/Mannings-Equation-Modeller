package main;

import ui.ScreenSwitcher;

public class ProgramScreenSwitcher extends ScreenSwitcher
{
	public ProgramScreenSwitcher(CrossSectionModel model)
	{
		super(new InputScreen(model.getData(), model.getPrecision(), model.getCalculator()), "Input");
		this.addScreen(new ResultScreen(model.getData(), model.getCalculator()), "Results");
	}
}
