package main;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;

import data.DataPrecision;
import data.DiscreteData;
import data.MapDiscreteData;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.math.BigDecimal;

import graphs.Range;
import graphs.Graph;
import graphs.GraphContainer;
import hydraulics.WaterLevelChangeListener;
import hydraulics.WaterLevelCalculator;
import hydraulics.WaterLevelVisualiser;
import ui.BigDecimalGraphTableModel;
import ui.TableEditPanel;

/**
 * Contains methods for constructing input screen
**/

public class InputScreen extends JPanel
{
	private static final String X_LABEL = "Distance from bank (m)";
	private static final String Y_LABEL = "Elevation (m)";
	private static final String GRAPH_TITLE = "Cross-section of River Bank";

	public InputScreen(MapDiscreteData<BigDecimal, BigDecimal> data, DataPrecision precision, WaterLevelCalculator<BigDecimal, BigDecimal> waterCalculator)
	{
		super(new BorderLayout());

		// Init components
		GraphContainer graphContainer = this.createGraphContainer(this.createGraph(), precision);
		graphContainer.getGraph().getGraphComponents().add(new WaterLevelVisualiser(graphContainer.getGraph(), waterCalculator));
		this.add(graphContainer, BorderLayout.CENTER);
		JTable table = this.createTable(data, precision);
		this.add(this.createSidePanel(table, waterCalculator, precision.getY(), graphContainer.getGraph(), precision), BorderLayout.WEST);

		// Connect data to components
		this.addVisualData(graphContainer, data);
	}

	private JTable createTable(DiscreteData<BigDecimal, BigDecimal> data, DataPrecision precision)
	{
		JTable table = new JTable(new BigDecimalGraphTableModel(data, precision, InputScreen.X_LABEL, InputScreen.Y_LABEL));
		return table;
	}
	
	private JScrollPane initTablePane(JTable table)
	{
		JScrollPane panel = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// Default width: 200
		panel.setPreferredSize(new Dimension(200, 1080));
		return panel;
	}
	
	private JPanel createSidePanel(JTable table, WaterLevelCalculator<BigDecimal, BigDecimal> calculator, int spinnerPrecision, Graph graph, DataPrecision precision)
	{
		JPanel panel = new JPanel();

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(this.tableEditPanel(table, precision));
		panel.add(this.initTablePane(table), BorderLayout.WEST);
		panel.add(new JLabel("Water Level:"));
		panel.add(this.initWaterSpinner(calculator, spinnerPrecision, graph));

		return panel;
	}

	private TableEditPanel tableEditPanel(JTable table, DataPrecision precision)
	{
		return new TableEditPanel(table, precision);
	}


	private JSpinner initWaterSpinner(WaterLevelCalculator<BigDecimal, BigDecimal> calculator, int precision, Graph graph)
	{
		// Default range: 0 to 100
		// Default increment: 0.1
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(calculator.getWaterLevel().doubleValue(), 0d, 100d, Math.pow(0.1d, precision)));
		spinner.addChangeListener(new WaterLevelChangeListener(calculator, graph));
		return spinner;
	}

	private Graph createGraph()
	{
		Graph graph = new Graph();
		graph.setPreferredSize(new Dimension(500, 500));

		// Default dimensions: (0, 10) * (0, 5)
		graph.setLinearPlane(new Range(0, 10), new Range(0, 5));
		// Default scale: 2 * 0.5
		graph.fitGridPlane(2d, 0.5d);

		return graph;
	}
	
	private GraphContainer createGraphContainer(Graph graph, DataPrecision precision)
	{		
		GraphContainer container = new GraphContainer(graph, precision);
		container.getAxis(GraphContainer.Direction.BOTTOM).addTickmarks();
		container.getAxis(GraphContainer.Direction.BOTTOM).addNumbers();
		container.getAxis(GraphContainer.Direction.BOTTOM).addName(InputScreen.X_LABEL);
		container.getAxis(GraphContainer.Direction.LEFT).addTickmarks();
		container.getAxis(GraphContainer.Direction.LEFT).addNumbers();
		container.getAxis(GraphContainer.Direction.LEFT).addName(InputScreen.Y_LABEL);
		container.getAxis(GraphContainer.Direction.TOP).addName(InputScreen.GRAPH_TITLE);
		
		return container;
	}
	
	private void addVisualData(GraphContainer container, DiscreteData<?, ?> data)
	{
		container.getGraph().getDataList().addData(data);
		container.getGraph().getDataList().getVisualsHandler(data).plotData();
		container.getGraph().getDataList().getVisualsHandler(data).connectData();
	}
}
