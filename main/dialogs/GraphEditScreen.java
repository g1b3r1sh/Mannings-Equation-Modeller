package main.dialogs;

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

import data.Range;
import graphs.Graph;
import graphs.GraphContainer;
import graphs.Grid;
import graphs.axis.Axis;
import ui.RangeSpinnerController;
import ui.SpinnerController;
import ui.SpinnerWrapperController;
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
		super(new BorderLayout(5, 5));

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
		this.xScaleController = new SpinnerWrapperController<>(this.xScale, this);
		this.yScaleController = new SpinnerWrapperController<>(this.yScale, this);
		this.xTicks = new Wrapper<>(this.xAxis.getTickmarks().getNumTicks());
		this.yTicks = new Wrapper<>(this.yAxis.getTickmarks().getNumTicks());
		this.xTicksController = new SpinnerWrapperController<>(this.xTicks, this);
		this.yTicksController = new SpinnerWrapperController<>(this.yTicks, this);
		this.gridX = new Wrapper<>(this.getGrid().getNumCols());
		this.gridY = new Wrapper<>(this.getGrid().getNumRows());
		this.gridXController = new SpinnerWrapperController<>(this.gridX, this);
		this.gridYController = new SpinnerWrapperController<>(this.gridY, this);

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

		panel.add(this.createSpinnerPanel("X Scale: ", this.createIntegerSpinner(this.xScaleController, 0, null, 1)));
		panel.add(this.createSpinnerPanel("Y Scale: ", this.createIntegerSpinner(this.yScaleController, 0, null, 1)));
		
		panel.add(this.createSpinnerPanel("X Tickmarks: ", this.createIntegerSpinner(this.xTicksController, 2, null, 1)));
		panel.add(this.createSpinnerPanel("Y Tickmarks: ", this.createIntegerSpinner(this.yTicksController, 2, null, 1)));
		
		panel.add(this.createSpinnerPanel("Grid Columns: ", this.createIntegerSpinner(this.gridXController, 1, null, 1)));
		panel.add(this.createSpinnerPanel("Grid Rows: ", this.createIntegerSpinner(this.gridYController, 1, null, 1)));
		
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

	private JSpinner createIntegerSpinner(SpinnerController<Integer> controller, Integer min, Integer max, Number step)
	{
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(controller.getValue(), min, max, step));
		controller.setSpinner(spinner);
		this.widenSpinner(spinner);
		return spinner;
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
		((DefaultEditor) spinner.getEditor()).getTextField().setColumns(3);
	}
}
