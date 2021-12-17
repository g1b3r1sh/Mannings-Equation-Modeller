import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.math.BigDecimal;

import graphs.MapFunctionDataConnected;
import hydraulics.WaterLevelCalculator;
import ui.ScreenSwitcher;

/**
 * Main program
 * Constructs and launches main window of application
**/

public class Main
{
	private static final String FRAME_TITLE = "Hydraulic Analysis Program";
	
	public static void main(String[] args)
	{
		// Init data
		MapFunctionDataConnected<BigDecimal, BigDecimal> data = defaultData();
		WaterLevelCalculator<BigDecimal, BigDecimal> waterCalculator = new WaterLevelCalculator<BigDecimal, BigDecimal>(data, new BigDecimal("2.00"));
		
		// Init frame
		JFrame frame = initFrame();
		frame.setJMenuBar(initMenu());

		ScreenSwitcher switcher = new ScreenSwitcher(InputScreen.initInputPanel(data, waterCalculator), "Input");
		switcher.addScreen(ResultScreen.initResultPanel(data, waterCalculator), "Results");

		frame.add(switcher);

		launchFrame(frame);
	}

	public static JFrame initFrame()
	{
		JFrame frame = new JFrame(FRAME_TITLE);
		frame.setSize(1000, 800);
		frame.setLocation(10, 10);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		return frame;
	}

	public static void launchFrame(JFrame frame)
	{
		frame.setVisible(true);
	}

	public static MapFunctionDataConnected<BigDecimal, BigDecimal> defaultData()
	{

		MapFunctionDataConnected<BigDecimal, BigDecimal> data = new MapFunctionDataConnected<>(3, 2);
		
		data.set(new BigDecimal("0.254"), new BigDecimal("4.12"));
		data.set(new BigDecimal("1.000"), new BigDecimal("1.00"));
		data.set(new BigDecimal("2.000"), new BigDecimal("3.00"));
		data.set(new BigDecimal("4.234"), new BigDecimal("0.26"));
		data.set(new BigDecimal("5.723"), new BigDecimal("3.26"));
		data.set(new BigDecimal("6.320"), new BigDecimal("3.55"));
		data.set(new BigDecimal("8.242"), new BigDecimal("4.16"));
		data.set(new BigDecimal("9.121"), new BigDecimal("4.80"));
		
		return data;
	}

	public static JMenuBar initMenu()
	{
		JMenuBar menuBar = new JMenuBar();
		JMenu help = new JMenu("Help");
		menuBar.add(help);
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
				JOptionPane.showMessageDialog(menuBar.getRootPane(), message);
			}
		};
		help.add(helpItem("What is this?", "what", listener));
		help.add(helpItem("How to modify data", "table", listener));
		help.add(helpItem("How to modify water level", "level", listener));
		help.add(helpItem("How to see area of water", "area", listener));

		return menuBar;
	}

	public static JMenuItem helpItem(String title, String action, ActionListener listener)
	{
		JMenuItem item = new JMenuItem(title);
		item.setActionCommand(action);
		item.addActionListener(listener);
		return item;
	}
}