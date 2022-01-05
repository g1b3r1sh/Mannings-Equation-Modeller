package main;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindowMenu extends JMenuBar
{
	public MainWindowMenu()
	{
		super();
		this.add(this.helpJMenu());
	}

	private JMenu helpJMenu()
	{
		JMenu help = new JMenu("Help");
		ActionListener listener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (e.getActionCommand().equals("table"))
				{
					this.popup("To modify the data, select a cell in the table and press the \"Edit Cell\" button.\nThen, enter the value in the popup that appears. Make sure the input is a number!");
				}
				else if (e.getActionCommand().equals("level"))
				{
					this.popup("To modify water level on the graph, modify the number in the number field below the table.\nYou can also click on the arrows to increment it by small amounts. If the water cannot be \ncontained by the banks, it won't be drawn.");
				}
				else if (e.getActionCommand().equals("area"))
				{
					this.popup("To calculate the area of the water, click on the \"Results\" button in the bottom-right corner.\nTo go back, click on the \"Input\" button in the bottom-left corner");
				}
				else if (e.getActionCommand().equals("what"))
				{
					this.popup("This application calculates the area of the cross-section of a river. The plot on the graph represents\n the waterbed and the blue represent water. The waterlevel can be adjusted to calculate the area under different conditions.");
				}
			}

			private void popup(String message)
			{
				JOptionPane.showMessageDialog(null, message);
			}
		};
		help.add(helpItem("What is this?", "what", listener));
		help.add(helpItem("How to modify data", "table", listener));
		help.add(helpItem("How to modify water level", "level", listener));
		help.add(helpItem("How to see area of water", "area", listener));

		return help;
	}
	
	public static JMenuItem helpItem(String title, String action, ActionListener listener)
	{
		JMenuItem item = new JMenuItem(title);
		item.setActionCommand(action);
		item.addActionListener(listener);
		return item;
	}
}
