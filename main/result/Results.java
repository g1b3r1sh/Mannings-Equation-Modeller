package main.result;

import javax.swing.JLabel;

import hydraulics.WaterLevelCalculator;

import java.awt.Graphics;
import java.awt.Component;
import java.math.BigDecimal;

/**
 * JLabel that displays area of water in waterCalculator
**/

public class Results extends JLabel
{
	private WaterLevelCalculator<BigDecimal, BigDecimal> waterCalculator;
	private int precision = 3;

	public Results(WaterLevelCalculator<BigDecimal, BigDecimal> waterCalculator)
	{
		super(" ");
		this.waterCalculator = waterCalculator;
		this.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.setFont(this.getFont().deriveFont(50f));
	}

	public void updateText()
	{
		double value = waterCalculator.crossSectionArea();
		this.setText(String.format("Area: %." + this.precision + "f m^2", value));
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		this.updateText();
		super.paintComponent(g);
	}
}
