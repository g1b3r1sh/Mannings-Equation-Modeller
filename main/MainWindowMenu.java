package main;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import main.inputscreen.DataEditDialog;
import ui.EditDialog;

import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class MainWindowMenu extends JMenuBar
{
	MainWindow window;

	public MainWindowMenu(MainWindow window)
	{
		super();
		this.window = window;
		this.add(this.editJMenu());
		this.add(this.helpJMenu());
	}

	private JMenu editJMenu()
	{
		JMenu edit = new JMenu("Edit");
		edit.add(menuItem("New", new Runnable()
		{
			@Override
			public void run()
			{
				MainWindow window = MainWindowMenu.this.window;
				window.getScreenSwitcher().switchFirst();
				window.getInputScreen().getEditDialog().open(new Consumer<EditDialog>()
				{
					@Override
					public void accept(EditDialog t)
					{
						DataEditDialog dialog = (DataEditDialog) t;
						dialog.getEditScreen().getController().getNewTableAction().actionPerformed(null);
					}
				});
			}
		}));
		return edit;
	}

	private JMenu helpJMenu()
	{
		JMenu help = new JMenu("Help");
		
		help.add(helpItem("What is this?", 
			"""
			To modify the data, select a cell in the table and press the \"Edit Cell\" button.\n
			Then, enter the value in the popup that appears. Make sure the input is a number!
			"""
		));
		help.add(helpItem("How to modify data", 
			"""
			To modify water level on the graph, modify the number in the number field below the table.\n
			You can also click on the arrows to increment it by small amounts. If the water cannot be \n
			contained by the banks, it won't be drawn.
			"""
		));
		help.add(helpItem("How to modify water level", 
			"""
			To calculate the area of the water, click on the \"Results\" button in the bottom-right corner.\n
			To go back, click on the \"Input\" button in the bottom-left corner.
			"""
		));
		help.add(helpItem("How to see area of water", 
			"""
			This application calculates the area of the cross-section of a river. The plot on the graph represents\n
			 the waterbed and the blue represent water. The waterlevel can be adjusted to calculate the area under different conditions.
			"""
		));

		return help;
	}
	
	public JMenuItem helpItem(String title, String dialog)
	{
		return menuItem(title, new Runnable()
		{
			@Override
			public void run()
			{
				JOptionPane.showMessageDialog(MainWindowMenu.this.window, dialog);
			}
		});
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
