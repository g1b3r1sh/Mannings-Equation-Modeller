package main;

import javax.swing.JFrame;

import main.input.InputScreen;
import main.result.ResultScreen;

public class MainWindow extends JFrame
{
	private static final String FRAME_TITLE = "Hydraulic Analysis Program";

	private CrossSectionModel model;
	private ScreenSwitcher screenSwitcher;

	private InputScreen inputScreen;
	private ResultScreen resultScreen;

	public MainWindow(CrossSectionModel model)
	{
		super(MainWindow.FRAME_TITLE);

		this.model = model;

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000, 600);
		this.setLocation(10, 10);
		// this.setExtendedState(JFrame.MAXIMIZED_BOTH);

		this.inputScreen = new InputScreen(this, model);
		this.resultScreen = new ResultScreen(this, model.getData());

		this.screenSwitcher = this.createScreenSwitcher();
		this.switchInputScreen();
		this.initializeFrame();
	}

	public CrossSectionModel getModel()
	{
		return this.model;
	}

	public InputScreen getInputScreen()
	{
		return this.inputScreen;
	}

	public ResultScreen getResultScreen()
	{
		return this.resultScreen;
	}

	public void switchInputScreen()
	{
		this.screenSwitcher.switchScreen(0);
	}

	public void switchResultScreen()
	{
		this.screenSwitcher.switchScreen(1);
	}

	private void initializeFrame()
	{
		this.setJMenuBar(new MainWindowMenu(this));
		this.add(this.screenSwitcher);
	}

	private ScreenSwitcher createScreenSwitcher()
	{
		ScreenSwitcher switcher = new ScreenSwitcher();
		switcher.addScreen(this.inputScreen, "Cross-Section Dataset");
		switcher.addScreen(this.resultScreen, "Manning's Equation Model");
		return switcher;
	}
}
