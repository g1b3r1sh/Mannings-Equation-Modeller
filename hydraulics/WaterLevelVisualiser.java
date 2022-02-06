package hydraulics;

import java.awt.Graphics;
import java.awt.geom.Path2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.PathIterator;
import java.beans.PropertyChangeEvent;

import graphs.Graph;
import graphs.Plane;
import graphs.visualiser.DataVisualiser;

/**
 * Visualises water level in graph
**/

// Allow for adjusting out of bounds, but don't color anything
public class WaterLevelVisualiser extends DataVisualiser
{
	private CrossSectionCalculator<? extends Number, ? extends Number> calculator;
	private Color WATER_COLOR = Color.BLUE;
	
	public WaterLevelVisualiser(Graph graph, CrossSectionCalculator<? extends Number, ? extends Number> calculator)
	{
		super(graph, calculator.getSectionData());
		this.calculator = calculator;
		this.calculator.addPropertyChangeListener(this);

		this.setColor(this.WATER_COLOR);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		super.propertyChange(evt);
		if (evt.getSource() == this.calculator)
		{
			this.repaint();
		}
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(this.getColor());
		this.drawWater(g);
		g.drawLine(0, this.waterYConversion(), this.getWidth(), this.waterYConversion());
	}
	
	// Get all water segments from calculator and paint them on the graph
	private void drawWater(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;

		for (Path2D.Double poly : this.calculator.generateWaterPolygons())
		{
			g2.fill(this.transformPolygon(poly));
		}
	}
	
	// Transforms water segment from data plane to graph graphics plane
	private Path2D.Double transformPolygon(Path2D.Double poly)
	{
		Path2D.Double transformed = new Path2D.Double();
		Plane plane = this.getGraph().getPlane();
		PathIterator it = poly.getPathIterator(null);
		double[] coords = new double[6];
		it.currentSegment(coords);
		it.next();
		transformed.moveTo(plane.posX(coords[0]), plane.posY(coords[1]));
		while (it.currentSegment(coords) != PathIterator.SEG_CLOSE)
		{
			transformed.lineTo(plane.posX(coords[0]), plane.posY(coords[1]));
			it.next();
		}
		transformed.closePath();
		return transformed;
	}

	private int waterYConversion()
	{
		return this.getGraph().getPlane().posY(this.calculator.getWaterLevel());
	}
}
