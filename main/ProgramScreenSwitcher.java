package main;

import javax.swing.JFrame;

import main.input.InputScreen;
import main.result.ResultScreen;
import ui.ScreenSwitcher;

public class ProgramScreenSwitcher extends ScreenSwitcher
{
	private InputScreen inputScreen;

	public ProgramScreenSwitcher(JFrame parent, CrossSectionModel model)
	{
		super();
		this.inputScreen = new InputScreen(model.getData(), model.getPrecision(), model.getCalculator(), parent);
		this.addScreen(this.inputScreen, "Input");
		this.addScreen(new ResultScreen(model.getData()), "Results");
		this.switchFirst();
	}

	public InputScreen getInputScreen()
	{
		return this.inputScreen;
	}
}
