package main.input;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;

import data.DataPrecision;
import data.Range;
import data.functions.DiscreteData;
import data.functions.MapDiscreteData;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.math.BigDecimal;

import graphs.Graph;
import graphs.GraphContainer;
import graphs.GraphController;
import hydraulics.WaterLevelChangeListener;
import hydraulics.WaterLevelCalculator;
import hydraulics.WaterLevelVisualiser;
import main.dialogs.DataEditDialog;
import main.dialogs.DataEditScreen;
import main.dialogs.GraphEditDialog;
import main.dialogs.GraphEditScreen;
import spinner.PrecisionSpinnerModel;
import table.EditableDiscreteDataModel;

/**
 * Contains methods for constructing input screen
**/

public class InputScreen extends JPanel
{
	private static final String X_LABEL = "Distance from bank (m)";
	private static final String TABLE_X_LABEL = "<html>Distance from<br>bank(m)</html>";
	private static final String Y_LABEL = "Elevation (m)";
	private static final String GRAPH_TITLE = "Cross-section of River Bank";

	private DataEditDialog editDialog;
	private GraphEditDialog graphDialog;
	private EditableDiscreteDataModel tableModel;
	private Graph graph;
	private JSpinner waterLevelSpinner;
	private GraphController graphController;

	public InputScreen(MapDiscreteData<BigDecimal, BigDecimal> data, DataPrecision precision, WaterLevelCalculator<BigDecimal, BigDecimal> waterCalculator, JFrame parent)
	{
		super(new BorderLayout());

		this.editDialog = new DataEditDialog(parent, new DataEditScreen(data, precision, InputScreen.TABLE_X_LABEL, InputScreen.Y_LABEL));

		// Init components
		this.graph = this.createGraph();
		GraphContainer graphContainer = this.createGraphContainer(this.graph, precision);
		graphContainer.getGraph().getGraphComponents().add(new WaterLevelVisualiser(graphContainer.getGraph(), waterCalculator));
		this.add(graphContainer, BorderLayout.CENTER);
		
		this.graphController = new GraphController(graphContainer);
		this.graphDialog = new GraphEditDialog(parent, new GraphEditScreen(graphContainer));
		this.graphDialog.addPropertyChangeListener(this.graphController);

		this.tableModel = new EditableDiscreteDataModel(data, precision, InputScreen.TABLE_X_LABEL, InputScreen.Y_LABEL);

		JTable table = this.createTable();
		this.add(this.createSidePanel(table, waterCalculator, precision.getY(), graphContainer.getGraph(), precision), BorderLayout.WEST);

		// Connect data to components
		this.addVisualData(graphContainer, data);
	}

	public DataEditDialog getEditDialog()
	{
		return this.editDialog;
	}

	public GraphEditDialog getGraphEditDialog()
	{
		return this.graphDialog;
	}

	public EditableDiscreteDataModel getTableModel()
	{
		return this.tableModel;
	}

	public Graph getGraph()
	{
		return this.graph;
	}

	public JSpinner getWaterLevelSpinner()
	{
		return this.waterLevelSpinner;
	}

	private JTable createTable()
	{
		JTable table = new JTable(this.tableModel);
		table.setCellSelectionEnabled(true);
		table.getTableHeader().setReorderingAllowed(false);
		return table;
	}
	
	private JScrollPane createTablePane(JTable table)
	{
		JScrollPane pane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// Default width: 200
		pane.setPreferredSize(new Dimension(200, 1080));
		return pane;
	}
	
	private JPanel createSidePanel(JTable table, WaterLevelCalculator<BigDecimal, BigDecimal> calculator, int spinnerPrecision, Graph graph, DataPrecision precision)
	{
		JPanel panel = new JPanel();

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JButton button = new JButton(this.editDialog.createOpenAction("Edit"));
		panel.add(button);
		panel.add(this.createTablePane(table), BorderLayout.WEST);
		panel.add(new JLabel("Water Level:"));
		this.waterLevelSpinner = this.createWaterSpinner(calculator, spinnerPrecision, graph);
		panel.add(this.waterLevelSpinner);

		return panel;
	}


	private JSpinner createWaterSpinner(WaterLevelCalculator<BigDecimal, BigDecimal> calculator, int precision, Graph graph)
	{
		JSpinner spinner = new JSpinner(new PrecisionSpinnerModel(calculator.getWaterLevel().doubleValue(), null, null, precision));
		((DefaultEditor) spinner.getEditor()).getTextField().setFormatterFactory(new DefaultFormatterFactory(new DefaultFormatter()));
		spinner.addChangeListener(new WaterLevelChangeListener(calculator, graph));
		this.increaseFont(spinner, 5);

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

	private void increaseFont(Component component, float increment)
	{
		Font font = component.getFont();
		component.setFont(font.deriveFont(font.getSize2D() + increment));
	}
}
