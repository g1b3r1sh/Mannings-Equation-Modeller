package main;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import main.input.DataEditDialog;
import ui.EditDialog;
import ui.PrecisionSpinnerModel;

import java.awt.event.ActionEvent;
import java.util.function.Consumer;

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
		JMenu edit = new JMenu("Edit");
		edit.add(menuItem("New Dataset", new Runnable()
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
		edit.add(menuItem("Edit Dataset", new Runnable() {
			@Override
			public void run()
			{
				MainWindow window = MainWindowMenu.this.window;
				window.getScreenSwitcher().switchFirst();
				window.getInputScreen().getEditDialog().open();
			}
		}));
		edit.add(menuItem("Load Sample Dataset", new Runnable() {
			@Override
			public void run()
			{
				MainWindow window = MainWindowMenu.this.window;
				window.getScreenSwitcher().switchFirst();
				window.getModel().loadSampleData();
				window.getInputScreen().getTableModel().refresh();
				window.getInputScreen().getGraph().repaint();
				((PrecisionSpinnerModel) window.getInputScreen().getWaterLevelSpinner().getModel()).setValue(window.getModel().getCalculator().getWaterLevel());
			}
		}));
		return edit;
	}

	private JMenu windowMenu()
	{
		JMenu window = new JMenu("Window");
		window.add(menuItem("Edit Input Graph", new Runnable()
		{
			@Override
			public void run()
			{
				MainWindow window = MainWindowMenu.this.window;
				window.getScreenSwitcher().switchFirst();
				window.getInputScreen().getGraphEditDialog().open();
			}
		}));
		window.addSeparator();
		window.add(menuItem("Exit", new Runnable()
		{
			@Override
			public void run()
			{
				MainWindowMenu.this.window.dispose();
			}
		}));
		return window;
	}

	private JMenu helpMenu()
	{
		JMenu help = new JMenu("Help");
		
		help.add(helpItem("What is this?", 
			"""
			This application calculates the area of the cross-section of a river. The plot on the graph represents\n
			 the waterbed and the blue represent water. The water level can be adjusted to calculate the area under different conditions.
			"""
		));
		help.addSeparator();
		help.add(helpItem("How to modify data", 
			"""
			To modify the data, click the \"Edit\" button above the table to open the edit dialog. You can\n
			edit the data by selecting the cells and typing a number, click on the buttons in the panel to the right,\n
			or use keyboard shortcuts.
			"""
		));
		help.add(helpItem("Data Editing Keyboard Shortcuts",
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
		help.add(helpItem("How to modify water level", 
			"""
			To modify water level on the graph, modify the number in the number field below the table.\n
			You can also click on the arrows to increment it by small amounts. If the water cannot be \n
			contained by the banks, it won't be drawn.
			"""
		));
		// TODO: Add help for calculating

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
