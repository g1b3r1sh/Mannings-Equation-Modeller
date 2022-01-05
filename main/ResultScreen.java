package main;
import java.awt.Component;
import java.awt.Dimension;
import java.math.BigDecimal;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.MapDiscreteData;
import hydraulics.WaterLevelCalculator;
import ui.Results;

/**
 * Contains methods for constructing results screen
**/

public class ResultScreen extends JPanel
{
	public ResultScreen(MapDiscreteData<BigDecimal, BigDecimal> data, WaterLevelCalculator<BigDecimal, BigDecimal> waterCalculator)
	{
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(Box.createRigidArea(new Dimension(0, 30)));
		this.add(this.largeLabel("Results"));
		this.add(Box.createRigidArea(new Dimension(0, 30)));
		this.add(new Results(waterCalculator));
	}

	private JLabel largeLabel(String text)
	{
		JLabel label = new JLabel(text);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.setFont(label.getFont().deriveFont(50f));
		return label;
	}

}
