package main;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import main.dialogs.DataEditDialog;
import spinner.PrecisionSpinnerModel;

import java.awt.event.ActionEvent;

public class MainWindowMenu extends JMenuBar
{
	MainWindow window;

	public MainWindowMenu(MainWindow window)
	{
		super();
		this.window = window;
		this.add(this.editMenu());
		this.add(this.windowMenu());
		this.add(this.helpMenu());
	}

	private JMenu editMenu()
	{
		JMenu menu = new JMenu("Edit");
		menu.add(menuItem("New Dataset", () ->
		{
			this.window.getScreenSwitcher().switchInputScreen();
			this.window.getInputScreen().getEditDialog().open((dialog) ->
			{
				DataEditDialog dataEditDialog = (DataEditDialog) dialog;
				dataEditDialog.getEditScreen().getController().getNewTableAction().actionPerformed(null);
			});
		}));
		menu.add(menuItem("Edit Dataset", () ->
		{
			this.window.getScreenSwitcher().switchInputScreen();
			this.window.getInputScreen().getEditDialog().open();
		}));
		menu.add(menuItem("Load Sample Dataset", () ->
		{
			this.window.getScreenSwitcher().switchInputScreen();
			this.window.getModel().loadSampleData();
			this.window.getInputScreen().getTableModel().refresh();
			this.window.getInputScreen().getGraph().repaint();
			((PrecisionSpinnerModel) this.window.getInputScreen().getWaterLevelSpinner().getModel()).setValue(this.window.getModel().getCalculator().getWaterLevel());
		}));
		return menu;
	}

	private JMenu windowMenu()
	{
		JMenu menu = new JMenu("Window");
		menu.add(menuItem("Edit Input Graph", () ->
		{
				this.window.getScreenSwitcher().switchInputScreen();
				this.window.getInputScreen().getGraphEditDialog().open();
		}));
		menu.addSeparator();
		menu.add(menuItem("Exit", () -> { this.window.dispose(); }));
		return menu;
	}

	private JMenu helpMenu()
	{
		JMenu menu = new JMenu("Help");
		
		menu.add(helpItem("What is this?", 
			"""
			This application calculates the area of the cross-section of a river. The plot on the graph represents\n
			 the waterbed and the blue represent water. The water level can be adjusted to calculate the area under different conditions.
			"""
		));
		menu.addSeparator();
		menu.add(helpItem("How to modify data", 
			"""
			To modify the data, click the \"Edit\" button above the table to open the edit dialog. You can\n
			edit the data by selecting the cells and typing a number, click on the buttons in the panel to the right,\n
			or use keyboard shortcuts.
			"""
		));
		menu.add(helpItem("Data Editing Keyboard Shortcuts",
			"""
			This is a non-exhaustive list of possible shortcuts when editing table data:\n
			CTRL+C - Copy selected cells\n
			CTRL+V - Paste cells\n
			CTRL+X - Cut selected cells\n
			CTRL+D - Insert row
			CTRL+SHIFT+D - Add row to last
			DEL - Clear selected cells\n
			ESC - Cancel edit
			"""
		));
		menu.add(helpItem("How to modify water level", 
			"""
			To modify water level on the graph, modify the number in the number field below the table.\n
			You can also click on the arrows to increment it by small amounts. If the water cannot be \n
			contained by the banks, it won't be drawn.
			"""
		));

		return menu;
	}
	
	public JMenuItem helpItem(String title, String dialog)
	{
		return menuItem(title, () -> { JOptionPane.showMessageDialog(MainWindowMenu.this.window, dialog); });
	}

	public static JMenuItem menuItem(String title, Runnable action)
	{
		return new JMenuItem(new AbstractAction(title)
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				action.run();
			}
		});
	}
}
