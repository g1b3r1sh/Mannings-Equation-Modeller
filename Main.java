import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.math.BigDecimal;

import graphs.Range;
import graphs.MapFunctionDataConnected;
import graphs.DiscreteData;
import graphs.DiscreteDataRenderer;
import graphs.Graph;
import graphs.GraphContainer;
import graphs.GraphTableModel;
import hydraulics.WaterLevelCalculator;
import hydraulics.WaterLevelVisualiser;

class Main
{
	private static final String FRAME_TITLE = "Hydraulic Analysis Program";
	private static final String X_LABEL = "Distance from bank (m)";
	private static final String Y_LABEL = "Elevation (m)";
	private static final String GRAPH_TITLE = "Cross-section of River Bank";
	
	public static void main(String[] args)
	{
		// Init frame
		JFrame frame = initFrame();
		JPanel panel = initPanel();
		frame.add(panel);

		// Init data
		MapFunctionDataConnected<BigDecimal, BigDecimal> data = retrieveData();
		WaterLevelCalculator<BigDecimal, BigDecimal> waterCalculator = new WaterLevelCalculator<BigDecimal, BigDecimal>(data, new BigDecimal("2.00"));
		
		// Init components
		GraphContainer graphContainer = initGraphContainer();
		graphContainer.getGraph().getGraphComponents().add(new WaterLevelVisualiser(graphContainer.getGraph(), waterCalculator));
		panel.add(graphContainer, BorderLayout.CENTER);
		JTable table = initTable(data, 3, 2);
		panel.add(initSidePanel(table), BorderLayout.WEST);

		// Connect data to components
		addVisualData(graphContainer, data);

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

	public static <N extends Number, M extends Number> JTable initTable(DiscreteData<N, M> data, int precisionX, int precisionY)
	{
		JTable table = new JTable(new GraphTableModel<N, M>(data, X_LABEL, Y_LABEL));
		table.getColumnModel().getColumn(0).setCellRenderer(new DiscreteDataRenderer(data.getPrecisionX()));
		table.getColumnModel().getColumn(1).setCellRenderer(new DiscreteDataRenderer(data.getPrecisionY()));
		return table;
	}
	
	public static JScrollPane initTablePane(JTable table)
	{
		JScrollPane panel = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panel.setPreferredSize(new Dimension(200, 1080));
		return panel;
	}
	
	public static JPanel initSidePanel(JTable table)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(initTablePane(table), BorderLayout.WEST);
		return panel;
	}
	
	public static GraphContainer initGraphContainer()
	{
		Graph graph = new Graph(3, 2);
		graph.setPreferredSize(new Dimension(500, 500));
		// Default range
		graph.setLinearPlane(new Range(0, 10), new Range(0, 5));
		graph.fitGridPlane(2d, 1d);
		
		GraphContainer container = new GraphContainer(graph);
		container.addAxis(GraphContainer.Direction.BOTTOM);
		container.addNumbers(GraphContainer.Direction.BOTTOM);
		container.addAxisName(GraphContainer.Direction.BOTTOM, X_LABEL);
		container.addAxis(GraphContainer.Direction.LEFT);
		container.addNumbers(GraphContainer.Direction.LEFT);
		container.addAxisName(GraphContainer.Direction.LEFT, Y_LABEL);
		container.addAxisName(GraphContainer.Direction.TOP, GRAPH_TITLE);
		
		return container;
	}
	
	public static void addVisualData(GraphContainer container, DiscreteData<?, ?> data)
	{
		container.getGraph().getDataList().addData(data);
		container.getGraph().getDataList().getVisualsHandler(data).plotData();
		container.getGraph().getDataList().getVisualsHandler(data).connectData();
	}
	
	public static MapFunctionDataConnected<BigDecimal, BigDecimal> retrieveData()
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
}