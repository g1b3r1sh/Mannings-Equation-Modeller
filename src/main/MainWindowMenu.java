package main;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import main.dialogs.DataEditDialog;

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
			this.window.switchInputScreen();
			this.window.getInputScreen().getEditDialog().open((dialog) ->
			{
				DataEditDialog dataEditDialog = (DataEditDialog) dialog;
				dataEditDialog.getEditScreen().getController().getNewTableAction().actionPerformed(null);
			});
		}));
		menu.add(menuItem("Edit Dataset", () ->
		{
			this.window.switchInputScreen();
			this.window.getInputScreen().getEditDialog().open();
		}));
		menu.add(menuItem("Load Sample Dataset", () ->
		{
			this.window.switchInputScreen();
			this.window.getModel().loadSampleData();
		}));
		return menu;
	}

	private JMenu windowMenu()
	{
		JMenu menu = new JMenu("Window");
		menu.add(menuItem("Edit Input Graph", () ->
		{
				this.window.switchInputScreen();
				this.window.getInputScreen().openGraphEditDialog();
		}));
		menu.add(menuItem("Edit Output Graph", () ->
		{
				this.window.switchResultScreen();
				this.window.getResultScreen().openGraphEditDialog();
		}));
		menu.addSeparator();
		menu.add(menuItem("Exit", this.window::dispose));
		return menu;
	}

	private JMenu helpMenu()
	{
		JMenu menu = new JMenu("Help");
		
		menu.add(helpItem("About", 
			"""
			This application allows for the modeling the cross-section of the river. By passing discharge values to
			the program, it will use the Manning's Equation to output the water level associated with that discharge
			in the model.
			"""
		));
		menu.add(helpItem("Dataset Editing Keyboard Shortcuts",
			"""
			This is a non-exhaustive list of shortcuts when editing the model dataset:
			CTRL+C - Copy selected cells
			CTRL+V - Paste cells
			CTRL+X - Cut selected cells
			CTRL+D - Insert row
			CTRL+SHIFT+D - Add row to last
			DEL - Clear selected cells
			ESC - Cancel edit
			"""
		));
		menu.addSeparator();
		menu.add(helpItem("How to modify dataset", 
			"""
			To modify the dataset of the model, click the \"Edit\" button above the table to open the edit dialog. You can
			edit the data by selecting the cells and typing a number, click on the buttons in the panel to the right,
			or use keyboard shortcuts.
			"""
		));
		menu.add(helpItem("How to input discharge", 
			"""
			In the left column of the \"Manning's Equation Model\" screen, choose \"Single Output\" tab for inputting a single
			discharge value and the \"Table Output\" tab for inputting a range of discharge values. Input the discharge as
			specified under the tabs.
			"""
		));
		menu.add(helpItem("How to calculate water level", 
			"""
			Press the calculate button in the left column of the \"Manning's Equation Model\" screen. Wait for the calculating
			dialog that pops up to close and view the results under the calculate button. If discharge was inputted in \"Table Output\"
			mode, the discharge values will also show up on the rating curve graph to the right of the screen.
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

	public static JMenuItem menuItem(Action action)
	{
		return new JMenuItem(action);
	}
}
