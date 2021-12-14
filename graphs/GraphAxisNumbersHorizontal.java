package graphs;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

public class GraphAxisNumbersHorizontal extends GraphAxisNumbers
{
	public GraphAxisNumbersHorizontal(Graph graph, Range range) {
		super(graph, range);
	}

	@Override
	protected void modifyPreferredSize(Dimension size)
	{
		this.scaleFont();
		size.height = this.font.getSize();
	}

	@Override
	protected void paintNumbers(Graphics g, Rectangle bounds)
	{
		System.out.println(bounds);
		// Special case for numbers on sides
		g.drawString(this.getNumberString(0), 0, bounds.height);
		g.drawString(this.getNumberString(this.getRange().size()), bounds.width - g.getFontMetrics().stringWidth(this.getNumberString(1)), bounds.height);
	}
	
	@Override
	public boolean isOverlapping()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
