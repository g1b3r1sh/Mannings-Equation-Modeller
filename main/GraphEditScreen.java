package main;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import graphs.Graph;
import graphs.GraphContainer;
import graphs.Grid;
import graphs.Range;
import graphs.axis.Axis;
import ui.RangeSpinnerController;

import java.awt.BorderLayout;

public class GraphEditScreen extends JPanel implements ChangeListener
{
	private GraphContainer outsideGraphContainer;
	private GraphContainer previewGraphContainer;
	private RangeSpinnerController xRangeController;
	private RangeSpinnerController yRangeController;
	private Axis xAxis;
	private Axis yAxis;

	public GraphEditScreen(GraphContainer outsideGraphContainer)
	{
		super(new BorderLayout());

		// Setup graphs
		this.outsideGraphContainer = outsideGraphContainer;
		this.previewGraphContainer = new GraphContainer(new Graph());
		this.previewGraphContainer.getGraph().setPreferredSize(new Dimension(500, 500));
		this.lightCopyGraph(this.previewGraphContainer, this.outsideGraphContainer);

		this.xAxis = this.previewGraphContainer.getAxis(GraphContainer.Direction.BOTTOM);
		this.yAxis = this.previewGraphContainer.getAxis(GraphContainer.Direction.LEFT);
		
		this.xRangeController = new RangeSpinnerController(this.xAxis.getNumbers().getRange(), this);
		this.yRangeController = new RangeSpinnerController(this.yAxis.getNumbers().getRange(), this);

		this.add(this.previewGraphContainer, BorderLayout.CENTER);
		this.add(this.createSidePanel(), BorderLayout.WEST);
	}

	public void refresh()
	{
		this.lightCopyGraph(this.previewGraphContainer, this.outsideGraphContainer);
	}

	private void lightCopyGraph(GraphContainer copyContainer, GraphContainer outsideContainer)
	{
		Graph outsideGraph = outsideContainer.getGraph();
		Graph graph = copyContainer.getGraph();

		graph.setLinearPlane(outsideGraph.getPlane().getRangeX(), outsideGraph.getPlane().getRangeY());

		Grid outsideGrid = outsideGraph.getGrid();
		graph.setGrid(outsideGrid.getNumCols(), outsideGrid.getNumRows());
		
		this.lightCopyAxis(copyContainer, outsideContainer, GraphContainer.Direction.TOP);
		this.lightCopyAxis(copyContainer, outsideContainer, GraphContainer.Direction.LEFT);
		this.lightCopyAxis(copyContainer, outsideContainer, GraphContainer.Direction.RIGHT);
		this.lightCopyAxis(copyContainer, outsideContainer, GraphContainer.Direction.BOTTOM);
	}

	// Copy numbers and tickmarks, do not copy labels
	private void lightCopyAxis(GraphContainer copy, GraphContainer model, GraphContainer.Direction direction)
	{
		Axis copyAxis = copy.getAxis(direction);
		Axis modelAxis = model.getAxis(direction);
		copyAxis.setPrecision(modelAxis.getPrecision());
		if (modelAxis.containsTickmarks())
		{
			copyAxis.addTickmarks();
			copyAxis.getTickmarks().setNumTicks(modelAxis.getTickmarks().getNumTicks());
		}
		if (modelAxis.containsNumbers())
		{
			copyAxis.addNumbers();
			copyAxis.getNumbers().setPrecision(modelAxis.getNumbers().getPrecision());
		}
	}

	private JPanel createSidePanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel("X Axis"));
		panel.add(this.createRangeSpinners(this.xRangeController));
		panel.add(new JLabel("Y Axis"));
		panel.add(this.createRangeSpinners(this.yRangeController));
		return panel;
	}

	private JPanel createRangeSpinners(RangeSpinnerController controller)
	{
		Range range = controller.getRange();

		JSpinner lower = new JSpinner(new SpinnerNumberModel(range.getLower(), null, null, 1));
		JSpinner upper = new JSpinner(new SpinnerNumberModel(range.getUpper(), null, null, 1));
		((DefaultEditor) lower.getEditor()).getTextField().setColumns(2);
		((DefaultEditor) upper.getEditor()).getTextField().setColumns(2);
		controller.setLowerSpinner(lower);
		controller.setUpperSpinner(upper);

		JPanel panel = new JPanel(new FlowLayout());
		panel.add(new JLabel("Min: "));
		panel.add(lower);
		panel.add(new JLabel("Max: "));
		panel.add(upper);
		
		return panel;
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		if (e.getSource() == this.xRangeController)
		{
			this.xAxis.getNumbers().fitFont();
			this.previewGraphContainer.repaint();
		}
		else if (e.getSource() == this.yRangeController)
		{
			this.yAxis.getNumbers().fitFont();
			this.previewGraphContainer.repaint();
		}
	}
}
