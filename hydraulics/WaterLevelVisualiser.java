package hydraulics;

import java.awt.Graphics;
import java.awt.geom.Path2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.PathIterator;

import graphs.DataVisualiser;
import graphs.Graph;
import graphs.Plane;

// Allow for adjusting out of bounds, but don't color anything
public class WaterLevelVisualiser extends DataVisualiser
{
	private WaterLevelCalculator<? extends Number, ? extends Number> calculator;
	private Color WATER_COLOR = Color.BLUE;
	
	public WaterLevelVisualiser(Graph graph, WaterLevelCalculator<? extends Number, ? extends Number> calculator)
	{
		super(graph, calculator.getSectionData());
		this.calculator = calculator;

		this.setColor(this.WATER_COLOR);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(this.getColor());
		drawWater(g);
	}
	
	private void drawWater(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;

		if (this.calculator.withinBounds())
		{
			for (Path2D.Double poly : this.calculator.generateWaterPolygons())
			{
				g2.fill(this.transformPolygon(poly));
			}
		}
	}

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
}
