package hydraulics;

import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import graphs.Graph;

public class CalculatorSpinnerConnector implements ChangeListener
{
	private WaterLevelCalculator<?, ?> calculator;
	private Graph graph;

	public CalculatorSpinnerConnector(WaterLevelCalculator<?, ?> calculator, Graph graph)
	{
		this.calculator = calculator;
		this.graph = graph;
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		Object o = e.getSource();
		if (o instanceof JSpinner)
		{
			JSpinner spinner = (JSpinner) o;
			this.calculator.setWaterLevel((Double) spinner.getValue());
			this.graph.repaint();
		}
	}
	
}
