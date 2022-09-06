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
import spinner.RangeSpinnerController;
import spinner.SpinnerController;
import spinner.SpinnerWrapperController;
import utility.Wrapper;

import java.awt.BorderLayout;

public class GraphEditScreen extends JPanel implements ChangeListener
{
	private GraphContainer outsideGraphContainer;
	private GraphContainer previewGraphContainer = GraphEditScreen.createDefaultGraphContainer();
	private RangeSpinnerController xRangeController;
	private RangeSpinnerController yRangeController;
	private Axis xAxis = GraphEditScreen.getXAxis(this.previewGraphContainer);
	private Axis yAxis = GraphEditScreen.getYAxis(this.previewGraphContainer);
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
		this.previewGraphContainer.lightCopy(this.outsideGraphContainer);

		this.xRangeController = new RangeSpinnerController(this.xAxis.getNumbers().getRange());
		this.yRangeController = new RangeSpinnerController(this.yAxis.getNumbers().getRange());
		this.xScale = new Wrapper<>(this.xAxis.getScale());
		this.yScale = new Wrapper<>(this.yAxis.getScale());
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

	public void saveGraph()
	{
		this.outsideGraphContainer.lightCopy(this.previewGraphContainer);
	}

	public void reset()
	{
		this.previewGraphContainer.lightCopy(this.outsideGraphContainer);
		this.xScaleController.setValue(this.xAxis.getNumbers().getScale());
		this.yScaleController.setValue(this.yAxis.getNumbers().getScale());
		this.xTicksController.setValue(this.xAxis.getTickmarks().getNumTicks());
		this.yTicksController.setValue(this.yAxis.getTickmarks().getNumTicks());
		this.gridXController.setValue(this.getGrid().getNumCols());
		this.gridYController.setValue(this.getGrid().getNumRows());
	}
	
	@Override
	public void stateChanged(ChangeEvent e)
	{
		if (e.getSource() == this.xScaleController)
		{
			this.xAxis.getNumbers().setScale(this.xScale.value);
		}
		else if (e.getSource() == this.yScaleController)
		{
			this.yAxis.getNumbers().setScale(this.yScale.value);
		}
		else if (e.getSource() == this.xTicksController)
		{
			this.xAxis.getTickmarks().setNumTicks(this.xTicks.value);
			this.xAxis.getNumbers().fitFont();
		}
		else if (e.getSource() == this.yTicksController)
		{
			this.yAxis.getTickmarks().setNumTicks(this.yTicks.value);
			this.yAxis.getNumbers().fitFont();
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
		panel.add(GraphEditScreen.createRangeSpinners(this.xRangeController));
		panel.add(new JLabel("Y Axis"));
		panel.add(GraphEditScreen.createRangeSpinners(this.yRangeController));

		panel.add(GraphEditScreen.createSpinnerPanel("X Scale: ", GraphEditScreen.createIntegerSpinner(this.xScaleController, 0, null, 1)));
		panel.add(GraphEditScreen.createSpinnerPanel("Y Scale: ", GraphEditScreen.createIntegerSpinner(this.yScaleController, 0, null, 1)));
		
		panel.add(GraphEditScreen.createSpinnerPanel("X Tickmarks: ", GraphEditScreen.createIntegerSpinner(this.xTicksController, 2, null, 1)));
		panel.add(GraphEditScreen.createSpinnerPanel("Y Tickmarks: ", GraphEditScreen.createIntegerSpinner(this.yTicksController, 2, null, 1)));
		
		panel.add(GraphEditScreen.createSpinnerPanel("Grid Columns: ", GraphEditScreen.createIntegerSpinner(this.gridXController, 1, null, 1)));
		panel.add(GraphEditScreen.createSpinnerPanel("Grid Rows: ", GraphEditScreen.createIntegerSpinner(this.gridYController, 1, null, 1)));
		
		return panel;
	}

	private static JPanel createRangeSpinners(RangeSpinnerController controller)
	{
		Range range = controller.getRange();

		JSpinner lower = new JSpinner(new SpinnerNumberModel(range.getLower(), null, null, 1));
		JSpinner upper = new JSpinner(new SpinnerNumberModel(range.getUpper(), null, null, 1));
		GraphEditScreen.widenSpinner(lower);
		GraphEditScreen.widenSpinner(upper);
		controller.setLowerSpinner(lower);
		controller.setUpperSpinner(upper);

		JPanel panel = new JPanel(new FlowLayout());
		panel.add(new JLabel("Min: "));
		panel.add(lower);
		panel.add(new JLabel("Max: "));
		panel.add(upper);
		
		return panel;
	}

	private static JSpinner createIntegerSpinner(SpinnerController<Integer> controller, Integer min, Integer max, Number step)
	{
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(controller.getValue(), min, max, step));
		controller.setSpinner(spinner);
		GraphEditScreen.widenSpinner(spinner);
		return spinner;
	}

	private static JPanel createSpinnerPanel(String label, JSpinner spinner)
	{
		JPanel panel = new JPanel(new FlowLayout());
		panel.add(new JLabel(label));
		panel.add(spinner);
		return panel;
	}
	
	private static Axis getXAxis(GraphContainer container)
	{
		return container.getAxis(GraphContainer.Direction.BOTTOM);
	}

	private static Axis getYAxis(GraphContainer container)
	{
		return container.getAxis(GraphContainer.Direction.LEFT);
	}

	private Grid getGrid()
	{
		return this.previewGraphContainer.getGraph().getGrid();
	}

	private static void widenSpinner(JSpinner spinner)
	{
		((DefaultEditor) spinner.getEditor()).getTextField().setColumns(3);
	}

	private static GraphContainer createDefaultGraphContainer()
	{
		GraphContainer container = new GraphContainer(new Graph());
		container.getGraph().setPreferredSize(new Dimension(500, 500));
		container.getGraph().setLinearPlane(new Range(0, 1), new Range(0, 1));
		return container;
	}
}
