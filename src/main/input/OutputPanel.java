package main.input;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.DataScale;
import hydraulics.CrossSectionCalculator;

public class OutputPanel extends JPanel implements PropertyChangeListener
{
	private CrossSectionCalculator<BigDecimal, BigDecimal> calculator;
	private DataScale scale;

	private JLabel areaLabel = new JLabel();
	private JLabel perimeterLabel = new JLabel();

	public OutputPanel(CrossSectionCalculator<BigDecimal, BigDecimal> calculator, DataScale scale)
	{
		super();
		this.calculator = calculator;
		this.scale = scale;
		this.calculator.addPropertyChangeListener(this);
		this.scale.addPropertyChangeListener(this);

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(OutputPanel.displayPanel("Area (m^2)", this.areaLabel));
		this.add(OutputPanel.displayPanel("Wetted Perimeter (m)", this.perimeterLabel));
		this.updateLabels();
	}

	private static JPanel displayPanel(String name, JLabel label)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(new JLabel(String.format("%s: ", name)));
		panel.add(label);
		return panel;
	}

	private void updateLabels()
	{
		this.areaLabel.setText(this.fitLargestScale(new BigDecimal(this.calculator.crossSectionArea())).toString());
		this.perimeterLabel.setText(this.fitLargestScale(new BigDecimal(this.calculator.wettedPerimeter())).toString());
	}

	private BigDecimal fitLargestScale(BigDecimal decimal)
	{
		return this.scale.getX() >= this.scale.getY() ? this.scale.fitScaleX(decimal) : this.scale.fitScaleY(decimal);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		if (evt.getSource() == this.calculator || evt.getSource() == this.scale)
		{
			this.updateLabels();
		}
	}
}
