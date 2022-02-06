package main.input;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
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
import hydraulics.WaterLevelVisualiser;
import main.CrossSectionModel;
import main.dialogs.DataEditDialog;
import main.dialogs.DataEditScreen;
import main.dialogs.GraphEditDialog;
import main.dialogs.GraphEditScreen;
import spinner.ScaleSpinnerModel;
import table.DiscreteDataTableModel;
import table.EditableDiscreteDataModel;

/**
 * Contains methods for constructing input screen
**/

public class InputScreen extends JPanel
{
	private static final String X_LABEL = "Distance from bank (m)";
	private static final String TABLE_X_LABEL = "<html>Distance from<br>bank (m)</html>";
	private static final String Y_LABEL = "Elevation (m)";
	private static final String GRAPH_TITLE = "Cross-Section of River Bank";

	private DataEditDialog editDialog;
	private GraphEditDialog graphDialog;

	public InputScreen(JFrame parent, CrossSectionModel model)
	{
		super(new BorderLayout());

		this.editDialog = new DataEditDialog(parent, new DataEditScreen(model.getData(), model, InputScreen.TABLE_X_LABEL, InputScreen.Y_LABEL));

		// Init components
		Graph graph = InputScreen.createGraph();
		graph.getGraphComponents().add(new WaterLevelVisualiser(graph, model.getCalculator()));
		GraphContainer graphContainer = InputScreen.createGraphContainer(graph, model.getScale());
		this.add(graphContainer, BorderLayout.CENTER);

		this.graphDialog = new GraphEditDialog(parent, new GraphEditScreen(graphContainer));

		WaterLevelController waterLevelController = new WaterLevelController(model.getCalculator());
		waterLevelController.addScale(model.getScale());

		this.add(InputScreen.createSidePanel
			(
				waterLevelController,
				model,
				new EditableDiscreteDataModel(model.getData(), model.getScale(), InputScreen.TABLE_X_LABEL, InputScreen.Y_LABEL),
				this.editDialog
			),
			BorderLayout.WEST
		);

		// Connect data to components
		InputScreen.addVisualData(graph, model.getData());
	}

	public DataEditDialog getEditDialog()
	{
		return this.editDialog;
	}

	public void openGraphEditDialog()
	{
		this.graphDialog.open();
	}

	private static JTable createTable(DiscreteDataTableModel<?, ?> tableModel)
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

	private static JPanel createSidePanel(WaterLevelController controller, CrossSectionModel model, DiscreteDataTableModel<BigDecimal, BigDecimal> tableModel, DataEditDialog editDialog)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(InputScreen.createHeader("Cross-Section Data"));
		panel.add(InputScreen.centerComponent(InputScreen.createTablePane(InputScreen.createTable(tableModel))));
		panel.add(InputScreen.centerComponent(new JButton(editDialog.createOpenAction("Edit Dataset"))));
		panel.add(InputScreen.createHeader("Water Level:"));
		panel.add(InputScreen.centerComponent(InputScreen.createWaterSpinner(controller, model.getCalculator().getWaterLevel(), model.getScale().getY())));
		panel.add(InputScreen.createHeader("Cross-Section Values"));
		panel.add(InputScreen.centerComponent(new OutputPanel(model.getCalculator(), model.getScale())));

		return panel;
	}

	private static JSpinner createWaterSpinner(WaterLevelController waterLevelController, Number initialValue, int initialScale)
	{
		ScaleSpinnerModel model = new ScaleSpinnerModel(initialValue, null, null, initialScale);
		waterLevelController.addSpinnerModel(model);
		JSpinner spinner = new JSpinner(model);
		((DefaultEditor) spinner.getEditor()).getTextField().setFormatterFactory(new DefaultFormatterFactory(new DefaultFormatter()));
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

	private static void addVisualData(Graph graph, DiscreteData<?, ?> data)
	{
		graph.getDataList().addData(data);
		graph.getDataList().getVisualsHandler(data).plotData();
		graph.getDataList().getVisualsHandler(data).connectData();
	}

	private static JLabel createHeader(String string)
	{
		JLabel label = new JLabel(string);
		label.setFont(label.getFont().deriveFont(15f));
		return InputScreen.centerComponent(label);
	}

	private static <T extends JComponent> T centerComponent(T component)
	{
		component.setAlignmentX(Component.CENTER_ALIGNMENT);
		return component;
	}

	private static void increaseFont(Component component, float increment)
	{
		Font font = component.getFont();
		component.setFont(font.deriveFont(font.getSize2D() + increment));
	}
}
