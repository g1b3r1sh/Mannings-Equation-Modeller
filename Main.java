import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.math.BigDecimal;

import graphs.Range;
import graphs.MapFunctionData;
import graphs.Graph;
import graphs.GraphContainer;

import hydraulics.WaterLevelCalculator;
import hydraulics.WaterLevelVisualiser;

class Main
{
	private static String FRAME_TITLE = "Hydraulic Analysis Program";
	public static void main(String[] args)
	{
		JFrame frame = initFrame();
		frame.add(initPanel());



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

	public static JPanel initPanel()
	{
		JPanel panel = new JPanel(new BorderLayout());
		return panel;
	}

	public static JComponent initTable()
	{
		JTable table = new JTable();
		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		return scrollPane;
	}

	public static Graph initGraph()
	{
		// TODO: Set precision when loading data
		Graph graph = new Graph(3, 2);
		graph.setLinearPlane(new Range(0, 10), new Range(0, 0));
		return graph;
	}
}