package main;

import javax.swing.JFrame;

import main.input.InputScreen;
import main.result.ResultScreen;

public class MainScreenSwitcher extends ScreenSwitcher
{
	private InputScreen inputScreen;

	public MainScreenSwitcher(JFrame parent, CrossSectionModel model)
	{
		super();
		this.inputScreen = new InputScreen(model.getData(), model.getPrecision(), model.getCalculator(), parent);
		this.addScreen(this.inputScreen, "Input");
		this.addScreen(new ResultScreen(model.getData(), parent), "Calculate");
		this.switchFirst();
	}

	public InputScreen getInputScreen()
	{
		return this.inputScreen;
	}
}
