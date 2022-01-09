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
import ui.SpinnerController;
import ui.Wrapper;

import java.awt.BorderLayout;

public class GraphEditScreen extends JPanel implements ChangeListener
{
	private GraphContainer outsideGraphContainer;
	private GraphContainer previewGraphContainer;
	private RangeSpinnerController xRangeController;
	private RangeSpinnerController yRangeController;
	private Axis xAxis;
	private Axis yAxis;
	private Wrapper<Integer> xScale;
	private Wrapper<Integer> yScale;
	private SpinnerController<Integer> xScaleController;
	private SpinnerController<Integer> yScaleController;
	private Wrapper<Integer> xTicks;
	private Wrapper<Integer> yTicks;
	private SpinnerController<Integer> xTicksController;
	private SpinnerController<Integer> yTicksController;
	private Wrapper<Integer> gridX;
	private Wrapper<Integer> gridY;
	private SpinnerController<Integer> gridXController;
	private SpinnerController<Integer> gridYController;

	public GraphEditScreen(GraphContainer outsideGraphContainer)
	{
		super(new BorderLayout());

		// Setup graphs
		this.outsideGraphContainer = outsideGraphContainer;
		this.previewGraphContainer = new GraphContainer(new Graph());
		this.previewGraphContainer.getGraph().setPreferredSize(new Dimension(500, 500));
		this.previewGraphContainer.getGraph().setLinearPlane(new Range(0, 1), new Range(0, 1));
		this.previewGraphContainer.lightCopy(this.outsideGraphContainer);

		this.xAxis = this.getXAxis(this.previewGraphContainer);
		this.yAxis = this.getYAxis(this.previewGraphContainer);

		this.xRangeController = new RangeSpinnerController(this.xAxis.getNumbers().getRange(), this);
		this.yRangeController = new RangeSpinnerController(this.yAxis.getNumbers().getRange(), this);
		this.xScale = new Wrapper<>(this.xAxis.getPrecision());
		this.yScale = new Wrapper<>(this.yAxis.getPrecision());
		this.xScaleController = new SpinnerController<>(this.xScale, this);
		this.yScaleController = new SpinnerController<>(this.yScale, this);
		this.xTicks = new Wrapper<>(this.xAxis.getTickmarks().getNumTicks());
		this.yTicks = new Wrapper<>(this.yAxis.getTickmarks().getNumTicks());
		this.xTicksController = new SpinnerController<>(this.xTicks, this);
		this.yTicksController = new SpinnerController<>(this.yTicks, this);
		this.gridX = new Wrapper<>(this.getGrid().getNumCols());
		this.gridY = new Wrapper<>(this.getGrid().getNumRows());
		this.gridXController = new SpinnerController<>(this.gridX, this);
		this.gridYController = new SpinnerController<>(this.gridY, this);

		this.add(this.previewGraphContainer, BorderLayout.CENTER);
		this.add(this.createSidePanel(), BorderLayout.WEST);
	}

	public GraphContainer getPreviewGraph()
	{
		return this.previewGraphContainer;
	}

	public void refresh()
	{
		this.previewGraphContainer.lightCopy(this.outsideGraphContainer);
		this.xRangeController.setRange(this.xAxis.getNumbers().getRange());
		this.yRangeController.setRange(this.yAxis.getNumbers().getRange());
		this.xScaleController.setValue(this.xAxis.getNumbers().getPrecision());
		this.yScaleController.setValue(this.yAxis.getNumbers().getPrecision());
		this.xTicksController.setValue(this.xAxis.getTickmarks().getNumTicks());
		this.yTicksController.setValue(this.yAxis.getTickmarks().getNumTicks());
		this.gridXController.setValue(this.getGrid().getNumCols());
		this.gridYController.setValue(this.getGrid().getNumRows());
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
		else if (e.getSource() == this.xScaleController)
		{
			this.xAxis.getNumbers().setPrecision(this.xScale.value);
			this.xAxis.getNumbers().fitFont();
			this.previewGraphContainer.repaint();
		}
		else if (e.getSource() == this.yScaleController)
		{
			this.yAxis.getNumbers().setPrecision(this.yScale.value);
			this.yAxis.getNumbers().fitFont();
			this.previewGraphContainer.repaint();
		}
		else if (e.getSource() == this.xTicksController)
		{
			this.xAxis.getTickmarks().setNumTicks(this.xTicks.value);
			this.xAxis.repaint();
		}
		else if (e.getSource() == this.yTicksController)
		{
			this.yAxis.getTickmarks().setNumTicks(this.yTicks.value);
			this.yAxis.repaint();
		}
		else if (e.getSource() == this.gridXController)
		{
			this.getGrid().setNumCols(this.gridX.value);
			this.previewGraphContainer.getGraph().repaint();
		}
		else if (e.getSource() == this.gridYController)
		{
			this.getGrid().setNumRows(this.gridY.value);
			this.previewGraphContainer.getGraph().repaint();
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

		JSpinner scaleXSpinner = new JSpinner(new SpinnerNumberModel(this.xScale.value, 0, null, 1));
		this.widenSpinner(scaleXSpinner);
		this.xScaleController.addSpinner(scaleXSpinner);
		panel.add(this.createSpinnerPanel("X Scale: ", scaleXSpinner));
		JSpinner scaleYSpinner = new JSpinner(new SpinnerNumberModel(this.yScale.value, 0, null, 1));
		this.widenSpinner(scaleYSpinner);
		this.yScaleController.addSpinner(scaleYSpinner);
		panel.add(this.createSpinnerPanel("Y Scale: ", scaleYSpinner));
		
		JSpinner ticksXSpinner = new JSpinner(new SpinnerNumberModel(this.xTicks.value, 2, null, 1));
		this.widenSpinner(ticksXSpinner);
		this.xTicksController.addSpinner(ticksXSpinner);
		panel.add(this.createSpinnerPanel("X Tickmarks: ", ticksXSpinner));
		JSpinner ticksYSpinner = new JSpinner(new SpinnerNumberModel(this.yTicks.value, 2, null, 1));
		this.widenSpinner(ticksYSpinner);
		this.yTicksController.addSpinner(ticksYSpinner);
		panel.add(this.createSpinnerPanel("Y Tickmarks: ", ticksYSpinner));
		
		JSpinner gridXSpinner = new JSpinner(new SpinnerNumberModel(this.gridX.value, 1, null, 1));
		this.widenSpinner(gridXSpinner);
		this.gridXController.addSpinner(gridXSpinner);
		panel.add(this.createSpinnerPanel("Grid Columns: ", gridXSpinner));
		JSpinner gridYSpinner = new JSpinner(new SpinnerNumberModel(this.gridY.value, 1, null, 1));
		this.widenSpinner(gridYSpinner);
		this.gridYController.addSpinner(gridYSpinner);
		panel.add(this.createSpinnerPanel("Grid Rows: ", gridYSpinner));
		
		return panel;
	}

	private JPanel createRangeSpinners(RangeSpinnerController controller)
	{
		Range range = controller.getRange();

		JSpinner lower = new JSpinner(new SpinnerNumberModel(range.getLower(), null, null, 1));
		JSpinner upper = new JSpinner(new SpinnerNumberModel(range.getUpper(), null, null, 1));
		this.widenSpinner(lower);
		this.widenSpinner(upper);
		controller.setLowerSpinner(lower);
		controller.setUpperSpinner(upper);

		JPanel panel = new JPanel(new FlowLayout());
		panel.add(new JLabel("Min: "));
		panel.add(lower);
		panel.add(new JLabel("Max: "));
		panel.add(upper);
		
		return panel;
	}

	private JPanel createSpinnerPanel(String label, JSpinner spinner)
	{
		JPanel panel = new JPanel(new FlowLayout());
		panel.add(new JLabel(label));
		panel.add(spinner);
		return panel;
	}
	
	private Axis getXAxis(GraphContainer container)
	{
		return container.getAxis(GraphContainer.Direction.BOTTOM);
	}

	private Axis getYAxis(GraphContainer container)
	{
		return container.getAxis(GraphContainer.Direction.LEFT);
	}

	private Grid getGrid()
	{
		return this.previewGraphContainer.getGraph().getGrid();
	}

	private void widenSpinner(JSpinner spinner)
	{
		((DefaultEditor) spinner.getEditor()).getTextField().setColumns(2);
	}
}
