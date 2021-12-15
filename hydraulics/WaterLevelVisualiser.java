package hydraulics;

import java.awt.Graphics;

import graphs.DataVisualiser;
import graphs.DiscreteData;
import graphs.Graph;

public class WaterLevelVisualiser extends DataVisualiser
{
	private Number waterLevel;
	
	public WaterLevelVisualiser(Graph graph, DiscreteData<? extends Number, ? extends Number> data, Number waterLevel)
	{
		super(graph, data);
		this.waterLevel = waterLevel;
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		
	}
	
	private void drawWater(Graphics g, int fromIndex, int toIndex)
	{
		// TODO: Finish this
	}
}
