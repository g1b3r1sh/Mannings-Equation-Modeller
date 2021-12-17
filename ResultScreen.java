import java.awt.Component;
import java.awt.Dimension;
import java.math.BigDecimal;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import graphs.MapFunctionDataConnected;
import hydraulics.WaterLevelCalculator;
import ui.Results;

public class ResultScreen
{
	public static JPanel initResultPanel(MapFunctionDataConnected<BigDecimal, BigDecimal> data, WaterLevelCalculator<BigDecimal, BigDecimal> waterCalculator)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(Box.createRigidArea(new Dimension(0, 30)));
		panel.add(largeLabel("Results"));
		panel.add(Box.createRigidArea(new Dimension(0, 30)));
		panel.add(new Results(waterCalculator));

		return panel;
	}

	public static JLabel largeLabel(String text)
	{
		JLabel label = new JLabel(text);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.setFont(label.getFont().deriveFont(50f));
		return label;
	}

}
