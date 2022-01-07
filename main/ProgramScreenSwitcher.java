package main;

import javax.swing.JFrame;

import ui.ScreenSwitcher;

public class ProgramScreenSwitcher extends ScreenSwitcher
{
	public ProgramScreenSwitcher(JFrame parent, CrossSectionModel model)
	{
		super(new InputScreen(model.getData(), model.getPrecision(), model.getCalculator(), parent), "Input");
		this.addScreen(new ResultScreen(model.getData(), model.getCalculator()), "Results");
	}
}
