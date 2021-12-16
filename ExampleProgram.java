import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

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

class ExampleProgram
{
	public static void main(String[] args)
	{
		/// Frame init
		JFrame frame = new JFrame("Graph Demo");
		frame.setSize(1200, 800);
		frame.setLocation(10, 10);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		frame.add(panel);
	
		/// Graph init
		Graph graph = createGraph();
	
		GraphContainer container = new GraphContainer(graph);
		container.addAxis(GraphContainer.Direction.BOTTOM);
		container.addNumbers(GraphContainer.Direction.BOTTOM);
		container.addAxisName(GraphContainer.Direction.BOTTOM, "Distance from bank (m)");
		container.addAxis(GraphContainer.Direction.LEFT);
		container.addNumbers(GraphContainer.Direction.LEFT);
		container.addAxisName(GraphContainer.Direction.LEFT, "Water level (m)");
		container.addAxisName(GraphContainer.Direction.TOP, "Cross-section of River Bank");

		panel.add(container, BorderLayout.CENTER);
	
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
	}

	public static JLabel centeredXLabel(String text)
	{
		JLabel label = new JLabel(text);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		return label;
	}

	public static JLabel centeredYLabel(String text)
	{
		JLabel label = new JLabel(text);
		label.setAlignmentY(Component.CENTER_ALIGNMENT);
		return label;
	}

	public static Graph createGraph()
	{
		Range rangeX = new Range(0, 10);
		Range rangeY = new Range(0, 5);
		
		MapFunctionData<BigDecimal, BigDecimal> data = new MapFunctionData<>();
		data.set(new BigDecimal("0.254"), new BigDecimal("4.12"));
		data.set(new BigDecimal("1.124"), new BigDecimal("1.21"));
		data.set(new BigDecimal("2.523"), new BigDecimal("2.72"));
		data.set(new BigDecimal("4.234"), new BigDecimal("0.26"));
		data.set(new BigDecimal("5.723"), new BigDecimal("3.26"));
		data.set(new BigDecimal("6.320"), new BigDecimal("4.55"));
		data.set(new BigDecimal("8.242"), new BigDecimal("2.16"));
		data.set(new BigDecimal("9.121"), new BigDecimal("4.80"));
	
		Graph graph = new Graph(2, 2);
		graph.setPreferredSize(new Dimension(300, 300));
		
		graph.setLinearPlane(rangeX, rangeY);
		graph.setGrid(rangeX.size(), rangeY.size());
		graph.getDataList().addData(data);
		graph.getGraphComponents().add(new WaterLevelVisualiser(graph, new WaterLevelCalculator<BigDecimal, BigDecimal>(data, new BigDecimal("2.50"))));
		graph.getDataList().getVisualsHandler(data).plotData();
		graph.getDataList().getVisualsHandler(data).connectData();

	
		return graph;
	}
	
	public static void addMenuBar(JFrame frame)
	{
		JMenuBar menu = new JMenuBar();
		menu.add(new JMenu("JMenu"));
		menu.add(new JMenuItem("JMenuItem"));
		frame.setJMenuBar(menu);
	}
}