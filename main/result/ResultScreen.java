package main.result;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import data.MapDiscreteData;
import graphs.Graph;
import hydraulics.ManningsModel;
import ui.Wrapper;

/**
 * Contains methods for constructing results screen
**/

public class ResultScreen extends JPanel
{
	private static final String DEFAULT_N = "0.025";
	private static final String DEFAULT_S = "1";
	private static final String DEFAULT_Q = "1";
	private static final String DEFAULT_LEVEL = "0";
	private static final String DEFAULT_V = "0";
	private static final double DEFAULT_STEP = 0.001;
	private static final int DEFAULT_SCALE = 6;

	private ManningsModel model;
	private Wrapper<BigDecimal> n;
	private Wrapper<BigDecimal> s;
	private Wrapper<BigDecimal> q;
	private Wrapper<BigDecimal> level;
	private Wrapper<BigDecimal> a;
	private Wrapper<BigDecimal> v;

	private JLabel levelLabel;
	private JLabel vLabel;
	private JLabel aLabel;

	public ResultScreen(MapDiscreteData<BigDecimal, BigDecimal> data)
	{
		super();
		this.setLayout(new BorderLayout(10, 10));
		this.model = new ManningsModel(data);
		this.n = new Wrapper<>(new BigDecimal(ResultScreen.DEFAULT_N));
		this.s = new Wrapper<>(new BigDecimal(ResultScreen.DEFAULT_S));
		this.q = new Wrapper<>(new BigDecimal(ResultScreen.DEFAULT_Q));
		this.level = new Wrapper<>(new BigDecimal(ResultScreen.DEFAULT_LEVEL));
		this.a = new Wrapper<>(new BigDecimal(0));
		this.v = new Wrapper<>(new BigDecimal(ResultScreen.DEFAULT_V));

		this.levelLabel = new JLabel();
		this.aLabel = new JLabel();
		this.vLabel = new JLabel();

		this.add(this.createSidePanel(), BorderLayout.CENTER);
		// this.add(new Graph(), BorderLayout.CENTER);
	}

	public void refresh()
	{
		if (this.model.withinBounds(this.level.value.doubleValue()))
		{
			this.levelLabel.setText(this.level.value.toString());
			this.aLabel.setText(this.a.value.toString());
			this.vLabel.setText(this.v.value.toString());
		}
		else
		{
			this.levelLabel.setText("Overflow!");
			this.aLabel.setText("Overflow!");
			this.vLabel.setText("Overflow!");
		}
	}

	public JPanel createSidePanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(this.numberEditPanel("Manning's Constant", this.n));
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(this.numberEditPanel("Channel Bed Slope", this.s));
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(this.numberEditPanel("Cross-Section Discharge (m^3/s)", this.q));
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		JButton calculate = new JButton(this.calculateAction());
		calculate.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(calculate);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(this.numberPanel("Water Level Elevation (m)", this.level.value.toString(), this.levelLabel));
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(this.numberPanel("Cross-Section Area (m^2)", "0", this.aLabel));
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(this.numberPanel("Velocity (m/s)", this.v.value.toString(), this.vLabel));
		return panel;
	}

	public JPanel numberPanel(String name, String defaultString, JLabel numberLabel)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		// panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(new JLabel(String.format("%s: ", name)));
		numberLabel.setText(defaultString);
		panel.add(numberLabel);
		return panel;
	}

	public JPanel numberEditPanel(String name, Wrapper<BigDecimal> number)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		// panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel numberText = new JLabel(number.value.toString());
		JButton editButton = new JButton(new AbstractAction("Edit")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String output = JOptionPane.showInputDialog(ResultScreen.this, "Input new value", number.value.toString());
				if (output != null)
				{
					try
					{
						number.value = new BigDecimal(output);
						numberText.setText(number.value.toString());
					}
					catch (NumberFormatException exception) {}
				}
			}
		});

		panel.add(editButton); // Edit button
		panel.add(Box.createRigidArea(new Dimension(10, 0)));
		panel.add(new JLabel(String.format("%s: ", name)));
		panel.add(numberText);
		return panel;
	}

	public Action calculateAction()
	{
		return new AbstractAction("Calculate")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ManningsModel model = ResultScreen.this.model;
				double q = ResultScreen.this.q.value.doubleValue();
				model.setN(ResultScreen.this.n.value);
				model.setS(ResultScreen.this.s.value);
				double level = model.calcWaterLevel(q, ResultScreen.DEFAULT_STEP);

				ResultScreen.this.level.value = new BigDecimal(level);
				ResultScreen.this.level.value = ResultScreen.this.level.value.setScale(ResultScreen.DEFAULT_SCALE, RoundingMode.HALF_UP);
				ResultScreen.this.a.value = new BigDecimal(model.calcArea(level));
				ResultScreen.this.a.value = ResultScreen.this.a.value.setScale(ResultScreen.DEFAULT_SCALE, RoundingMode.HALF_UP);
				ResultScreen.this.v.value = new BigDecimal(model.calcVelocity(q, level));
				ResultScreen.this.v.value = ResultScreen.this.v.value.setScale(ResultScreen.DEFAULT_SCALE, RoundingMode.HALF_UP);
				
				ResultScreen.this.refresh();
			}
		};
	}
}
