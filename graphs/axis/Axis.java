package graphs.axis;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import graphs.Graph;
import graphs.GraphContainer;
import graphs.GraphContainer.Direction;

import java.awt.Component;
import java.util.function.Consumer;
import java.util.function.Function;


public class Axis extends JPanel
{
	private final static int DEFAULT_HORIZONTAL_PADDING = 10;
	private final static int DEFAULT_VERTICAL_PADDING = 10;
	private final static int DEFAULT_PRECISION = 2;
	private final static double DEFAULT_SCALE = 2.0;

	private GraphContainer graphContainer;
	private Direction direction;
	private int precision;
	private double scale;

	public Axis(GraphContainer graphContainer, Direction direction, int precision, double scale)
	{
		this.graphContainer = graphContainer;
		this.direction = direction;
		this.precision = precision;
		this.scale = scale;

		this.setLayout(new BoxLayout(this, this.horizontal() ? BoxLayout.Y_AXIS : BoxLayout.X_AXIS));
	}

	public Axis(GraphContainer graphContainer, Direction direction)
	{
		this(graphContainer, direction, Axis.DEFAULT_PRECISION, Axis.DEFAULT_SCALE);
	}

	public Graph getGraph()
	{
		return this.graphContainer.getGraph();
	}

	public int getPrecision()
	{
		return this.precision;
	}

	public void setPrecision(int precision)
	{
		this.precision = precision;
		// Update precision of Numbers and repaint it
	}

	public double getScale()
	{
		return this.scale;
	}

	public void setScale(double scale)
	{
		this.scale = scale;
	}

	public boolean containsTickmarks()
	{
		for (Component c : this.getComponents())
		{
			if (c instanceof AxisTickmarks)
			{
				return true;
			}
		}
		return false;
	}
	
	public void addTickmarks()
	{
		if (!this.containsTickmarks())
		{
			this.addAxisComponent(this.createTickmarks(), 0);
		}
	}

	// Returns null if axis doesn't exist
	public AxisTickmarks getTickmarks()
	{
		if (this.containsTickmarks())
		{
			return (AxisTickmarks) this.getAxisComponent(0);
		}
		return null;
	}

	public void removeTickmarks()
	{
		if (this.containsTickmarks())
		{
			this.removeAxisComponent(0);
			if (this.containsNumbers())
			{
				this.removeAxisComponent(1);
			}
		}
	}
	
	public boolean containsNumbers()
	{
		for (Component c : this.getComponents())
		{
			if (c instanceof AxisNumbers)
			{
				return true;
			}
		}
		return false;
	}

	public void addNumbers()
	{
		if (this.containsTickmarks() && !this.containsNumbers())
		{
			AxisNumbers numbers;
			if (this.horizontal())
			{
				numbers = new AxisNumbersHorizontal(this.getTickmarks(), this.getGraph().getPlane().getRangeX(), this.precision, Axis.DEFAULT_HORIZONTAL_PADDING);
			}
			else
			{
				numbers = new AxisNumbersVertical(this.getTickmarks(), this.getGraph().getPlane().getRangeY(), this.precision, Axis.DEFAULT_VERTICAL_PADDING, direction == Direction.LEFT);
			}
			this.addAxisComponent(numbers, 1);
		}
	}

	public void removeNumbers()
	{
		if (this.containsNumbers())
		{
			this.removeAxisComponent(1);
		}
	}

	public void addName(String name)
	{
		JLabel label = new JLabel(name);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.setAlignmentY(Component.CENTER_ALIGNMENT);
		this.addAxisComponent(label);
	}

	public void removeName()
	{
		if (!(this.getAxisComponent() instanceof AxisTickmarks))
		{
			this.removeAxisComponent();
		}
	}

	public AxisTickmarks createTickmarks()
	{
		switch (direction)
		{
			case BOTTOM:
				return new AxisTickmarksBottom(this);
			case LEFT:
				return new AxisTickmarksLeft(this);
			case RIGHT:
				return new AxisTickmarksRight(this);
			case TOP:
				return new AxisTickmarksTop(this);
			default:
				return null;
		}
	}

	public boolean inverse()
	{
		return this.direction == Direction.LEFT || this.direction == Direction.TOP;
	}

	public boolean horizontal()
	{
		return this.direction == Direction.TOP || this.direction == Direction.BOTTOM;
	}
	
	private <T> T directionPanelMethod(Function<JPanel, T> normal, Function<JPanel, T> inverse)
	{
		if (!this.inverse())
		{
			return normal.apply(this);
		}
		else
		{
			return inverse.apply(this);
		}
	}

	// Since java can't deduce void >:(
	private void directionPanelMethodVoid(Consumer<JPanel> normal, Consumer<JPanel> inverse)
	{
		if (!this.inverse())
		{
			normal.accept(this);
		}
		else
		{
			inverse.accept(this);
		}
	}

	private void addAxisComponent(JComponent component, int index)
	{
		this.directionPanelMethodVoid(
			(panel) -> panel.add(component, index), 
			(panel) -> panel.add(component, panel.getComponentCount() - index)
		);
	}

	// Adds component to last
	private void addAxisComponent(JComponent component)
	{
		this.directionPanelMethodVoid(
			(panel) -> panel.add(component), 
			(panel) -> panel.add(component, 0)
		);
	}

	private Component getAxisComponent(int index)
	{
		return this.directionPanelMethod(
			(panel) -> panel.getComponent(index),
			(panel) -> panel.getComponent(panel.getComponentCount() - 1 - index)
		);
	}

	// Gets last component
	private Component getAxisComponent()
	{
		return this.directionPanelMethod(
			(panel) -> panel.getComponent(panel.getComponentCount() - 1),
			(panel) -> panel.getComponent(0)
		);
	}

	private void removeAxisComponent(int index)
	{
		this.directionPanelMethodVoid(
			(panel) -> panel.remove(index), 
			(panel) -> panel.remove(panel.getComponentCount() - 1 - index)
		);
	}

	// Removes last component
	private void removeAxisComponent()
	{
		this.directionPanelMethodVoid(
			(panel) -> panel.remove(panel.getComponentCount() - 1), 
			(panel) -> panel.remove(0)
		);
	}
}