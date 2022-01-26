package main;

import javax.swing.JFrame;

import main.input.InputScreen;

public class MainWindow extends JFrame
{
	private static final String FRAME_TITLE = "Hydraulic Analysis Program";

	private CrossSectionModel model;
	MainScreenSwitcher screenSwitcher;

	public MainWindow(CrossSectionModel model)
	{
		super(MainWindow.FRAME_TITLE);

		this.model = model;

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000, 600);
		this.setLocation(10, 10);
		// this.setExtendedState(JFrame.MAXIMIZED_BOTH);

		this.screenSwitcher = new MainScreenSwitcher(this, this.model);
		this.initializeFrame();
	}

	public CrossSectionModel getModel()
	{
		return this.model;
	}

	public MainScreenSwitcher getScreenSwitcher()
	{
		return this.screenSwitcher;
	}

	public InputScreen getInputScreen()
	{
		return this.screenSwitcher.getInputScreen();
	}

	private void initializeFrame()
	{
		this.setJMenuBar(new MainWindowMenu(this));
		this.add(this.screenSwitcher);
	}
}
