import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;

import data.DiscreteData;
import data.MapDiscreteData;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.math.BigDecimal;

import graphs.Range;
import graphs.Graph;
import graphs.GraphContainer;
import hydraulics.CalculatorSpinnerConnector;
import hydraulics.DataPrecision;
import hydraulics.WaterLevelCalculator;
import hydraulics.WaterLevelVisualiser;
import ui.BigDecimalGraphTableModel;
import ui.PrecisionDataRenderer;
import ui.TableEditPanel;

/**
 * Contains methods for constructing input screen
**/

public class InputScreen
{
	private static final String X_LABEL = "Distance from bank (m)";
	private static final String Y_LABEL = "Elevation (m)";
	private static final String GRAPH_TITLE = "Cross-section of River Bank";

	public static JPanel initInputPanel(MapDiscreteData<BigDecimal, BigDecimal> data, DataPrecision precision, WaterLevelCalculator<BigDecimal, BigDecimal> waterCalculator)
	{
		JPanel panel = new JPanel(new BorderLayout());

		// Init components
		GraphContainer graphContainer = initGraphContainer();
		graphContainer.getGraph().getGraphComponents().add(new WaterLevelVisualiser(graphContainer.getGraph(), waterCalculator));
		panel.add(graphContainer, BorderLayout.CENTER);
		JTable table = initTable(data, precision, 3, 2);
		panel.add(initSidePanel(table, waterCalculator, precision.getPrecisionY(), graphContainer.getGraph(), precision), BorderLayout.WEST);

		// Connect data to components
		addVisualData(graphContainer, data);

		return panel;
	}

	public static JTable initTable(DiscreteData<BigDecimal, BigDecimal> data, DataPrecision precision, int precisionX, int precisionY)
	{
		JTable table = new JTable(new BigDecimalGraphTableModel(data, precision, X_LABEL, Y_LABEL));
		table.getColumnModel().getColumn(0).setCellRenderer(new PrecisionDataRenderer(precision.getPrecisionX()));
		table.getColumnModel().getColumn(1).setCellRenderer(new PrecisionDataRenderer(precision.getPrecisionY()));
		return table;
	}
	
	public static JScrollPane initTablePane(JTable table)
	{
		JScrollPane panel = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panel.setPreferredSize(new Dimension(200, 1080));
		return panel;
	}
	
	public static JPanel initSidePanel(JTable table, WaterLevelCalculator<BigDecimal, BigDecimal> calculator, int spinnerPrecision, Graph graph, DataPrecision precision)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(tableEditPanel(table, precision));
		panel.add(initTablePane(table), BorderLayout.WEST);
		panel.add(new JLabel("Water Level:"));
		panel.add(initWaterSpinner(calculator, spinnerPrecision, graph));
		return panel;
	}

	public static TableEditPanel tableEditPanel(JTable table, DataPrecision precision)
	{
		return new TableEditPanel(table, precision);
	}


	public static JSpinner initWaterSpinner(WaterLevelCalculator<BigDecimal, BigDecimal> calculator, int precision, Graph graph)
	{
		// TODO: Set min and max
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(calculator.getWaterLevel().doubleValue(), 0d, 100d, Math.pow(0.1d, precision)));
		spinner.addChangeListener(new CalculatorSpinnerConnector(calculator, graph));
		// TODO: Format
		return spinner;
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
}
