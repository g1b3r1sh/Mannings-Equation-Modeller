package main.result;

import java.awt.Color;
import java.awt.Graphics;

import graphs.Graph;
import graphs.GraphComponent;
import graphs.Plane;
import main.result.ManningsResultModel.Result;

public class ResultsVisualiser extends GraphComponent
{
	private Result[] data = null;
	private int circleSize = 10;

	public ResultsVisualiser(Graph graph)
	{
		super(graph);
	}

	public void setResults(Result[] results)
	{
		this.data = results;
		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		if (this.data == null)
		{
			return;
		}

		g.setColor(Color.BLACK);
		Plane plane = this.getGraph().getPlane();
		for (Result result : this.data)
		{
			if (result.getQ() != null && result.getLevel() != null)
			{
				this.paintPlotSymbol(g, plane.posX(result.getQ()), plane.posY(result.getLevel()));
			}
		}
	}

	protected void paintPlotSymbol(Graphics g, int centerX, int centerY)
	{
		g.fillOval(centerX - this.circleSize / 2, centerY - this.circleSize / 2, this.circleSize, this.circleSize);
	}
}
