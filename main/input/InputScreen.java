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

import data.DataScale;
import data.Range;
import data.functions.DiscreteData;

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
import main.CrossSectionModel;
import main.dialogs.DataEditDialog;
import main.dialogs.DataEditScreen;
import main.dialogs.GraphEditDialog;
import main.dialogs.GraphEditScreen;
import spinner.ScaleSpinnerModel;
import table.EditableDiscreteDataModel;

/**
 * Contains methods for constructing input screen
**/

public class InputScreen extends JPanel
{
	private static final String X_LABEL = "Distance from bank (m)";
	private static final String TABLE_X_LABEL = "<html>Distance from<br>bank(m)</html>";
	private static final String Y_LABEL = "Elevation (m)";
	private static final String GRAPH_TITLE = "Cross-Section of River Bank";

	private DataEditDialog editDialog;
	private GraphEditDialog graphDialog;
	private EditableDiscreteDataModel tableModel;
	private Graph graph;
	private JSpinner waterLevelSpinner;
	private GraphController graphController;

	public InputScreen(JFrame parent, CrossSectionModel model)
	{
		super(new BorderLayout());

		this.editDialog = new DataEditDialog(parent, new DataEditScreen(model.getData(), model.getScale(), InputScreen.TABLE_X_LABEL, InputScreen.Y_LABEL));

		// Init components
		this.graph = InputScreen.createGraph();
		this.graph.getGraphComponents().add(new WaterLevelVisualiser(this.graph, model.getCalculator()));
		GraphContainer graphContainer = InputScreen.createGraphContainer(this.graph, model.getScale());
		this.add(graphContainer, BorderLayout.CENTER);
		
		this.graphController = new GraphController(graphContainer);
		this.graphDialog = new GraphEditDialog(parent, new GraphEditScreen(graphContainer));
		this.graphDialog.addPropertyChangeListener(this.graphController);

		this.tableModel = new EditableDiscreteDataModel(model.getData(), model.getScale(), InputScreen.TABLE_X_LABEL, InputScreen.Y_LABEL);
		this.waterLevelSpinner = InputScreen.createWaterSpinner(model.getCalculator(), model.getScale().getY(), this.graph);

		this.add(this.createSidePanel(), BorderLayout.WEST);

		// Connect data to components
		this.addVisualData(model.getData());
	}

	public DataEditDialog getEditDialog()
	{
		return this.editDialog;
	}

	public void openGraphEditDialog()
	{
		this.graphDialog.open();
	}

	public void refreshTableModel()
	{
		this.tableModel.refreshData();
	}

	public void repaintGraph()
	{
		this.graph.repaint();
	}

	public JSpinner getWaterLevelSpinner()
	{
		return this.waterLevelSpinner;
	}

	private static JTable createTable(EditableDiscreteDataModel tableModel)
	{
		JTable table = new JTable(tableModel);
		table.setCellSelectionEnabled(true);
		table.getTableHeader().setReorderingAllowed(false);
		return table;
	}

	private static JScrollPane createTablePane(JTable table)
	{
		JScrollPane pane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// Default width: 200
		pane.setPreferredSize(new Dimension(200, 1080));
		return pane;
	}

	private JPanel createSidePanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(new JLabel("Cross-Section Data"));
		panel.add(InputScreen.createTablePane(InputScreen.createTable(this.tableModel)), BorderLayout.WEST);
		panel.add(new JButton(this.editDialog.createOpenAction("Edit Dataset")));
		panel.add(new JLabel("Water Level:"));
		panel.add(this.waterLevelSpinner);

		return panel;
	}

	private static JSpinner createWaterSpinner(WaterLevelCalculator<BigDecimal, BigDecimal> calculator, int scale, Graph graph)
	{
		JSpinner spinner = new JSpinner(new ScaleSpinnerModel(calculator.getWaterLevel().doubleValue(), null, null, scale));
		((DefaultEditor) spinner.getEditor()).getTextField().setFormatterFactory(new DefaultFormatterFactory(new DefaultFormatter()));
		spinner.addChangeListener(new WaterLevelChangeListener(calculator, graph));
		InputScreen.increaseFont(spinner, 5);

		return spinner;
	}

	private static Graph createGraph()
	{
		Graph graph = new Graph();
		graph.setPreferredSize(new Dimension(500, 500));

		// Default dimensions: (0, 10) * (0, 5)
		graph.setLinearPlane(new Range(0, 10), new Range(0, 5));
		// Default scale: 2 * 0.5
		graph.fitGridPlane(2d, 0.5d);

		return graph;
	}

	private static GraphContainer createGraphContainer(Graph graph, DataScale scale)
	{		
		GraphContainer container = new GraphContainer(graph, scale);
		container.getAxis(GraphContainer.Direction.BOTTOM).addTickmarks();
		container.getAxis(GraphContainer.Direction.BOTTOM).addNumbers();
		container.getAxis(GraphContainer.Direction.BOTTOM).addName(InputScreen.X_LABEL);
		container.getAxis(GraphContainer.Direction.LEFT).addTickmarks();
		container.getAxis(GraphContainer.Direction.LEFT).addNumbers();
		container.getAxis(GraphContainer.Direction.LEFT).addName(InputScreen.Y_LABEL);
		container.getAxis(GraphContainer.Direction.TOP).addName(InputScreen.GRAPH_TITLE);
		
		return container;
	}

	private void addVisualData(DiscreteData<?, ?> data)
	{
		this.graph.getDataList().addData(data);
		this.graph.getDataList().getVisualsHandler(data).plotData();
		this.graph.getDataList().getVisualsHandler(data).connectData();
	}

	private static void increaseFont(Component component, float increment)
	{
		Font font = component.getFont();
		component.setFont(font.deriveFont(font.getSize2D() + increment));
	}
}
