package hydraulics;

import java.awt.Graphics;

import graphs.DataVisualiser;
import graphs.Graph;

// Allow for adjusting out of bounds, but don't color anything
public class WaterLevelVisualiser extends DataVisualiser
{
	private WaterLevelCalculator<? extends Number, ? extends Number> calculator;
	
	public WaterLevelVisualiser(Graph graph, WaterLevelCalculator<? extends Number, ? extends Number> calculator)
	{
		super(graph, calculator.getSectionData());
		this.calculator = calculator;
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(this.getColor());
		g.drawLine(0, this.calcWaterY(), this.getWidth(), this.calcWaterY());
		// Request groups of points to color in
	}
	
	private void drawWater(Graphics g, int fromIndex, int toIndex)
	{
		if (this.calculator.withinBounds()){
			// TODO
		}
	}

	private int calcWaterY()
	{
		return this.getGraph().getPlane().posY(this.calculator.getWaterLevel());
	}
}
