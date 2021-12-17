import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.math.BigDecimal;

import graphs.MapFunctionDataConnected;
import hydraulics.WaterLevelCalculator;
import ui.ScreenSwitcher;

class Main
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
		help.add(new JMenuItem("No."));

		return menuBar;
	}
}