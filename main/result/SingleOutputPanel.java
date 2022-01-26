package main.result;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SingleOutputPanel extends OutputPanel
{
	private JLabel levelLabel;
	private JLabel vLabel;
	private JLabel errorLabel;

	public SingleOutputPanel(ResultScreen parent, ResultScreenController controller)
	{
		super(parent, controller);
		
		this.levelLabel = new JLabel();
		this.vLabel = new JLabel();
		this.errorLabel = new JLabel("");
		this.errorLabel.setForeground(Color.RED);
		this.errorLabel.setVisible(false);
		this.addComponents();
	}

	/// Panel methods

	private void addComponents()
	{
		this.add(this.inputPanel());
		this.add(ResultScreen.sidePadding());
		this.add(ResultScreen.sideButton(this.calculateAction()));
		this.add(ResultScreen.sidePadding());
		this.add(this.outputPanel());
	}

	private JPanel inputPanel()
	{
		JPanel panel = ResultScreen.mainSidePanel();
		panel.add(ResultScreen.numberEditPanel(this.getParentComponent(), "Cross-Section Discharge (m^3/s)", () -> this.getController().getQ(), (q) -> this.getController().setQ(q)));
		return panel;
	}

	private JPanel outputPanel()
	{
		JPanel panel = ResultScreen.mainSidePanel();
		panel.add(ResultScreen.componentPanel(this.errorLabel));
		panel.add(ResultScreen.sidePadding());
		panel.add(ResultScreen.numberDisplayPanel("Water Level Elevation (m)", "", this.levelLabel));
		panel.add(ResultScreen.sidePadding());
		panel.add(ResultScreen.numberDisplayPanel("Velocity (m/s)", "", this.vLabel));
		return panel;
	}

	private void showErrorMessage(String message)
	{
		this.errorLabel.setText(message);
		this.errorLabel.setVisible(true);
	}

	private void hideErrorMessage()
	{
		this.errorLabel.setText("");
		this.errorLabel.setVisible(false);
	}

	/// Processing Methods

	private Action calculateAction()
	{
		return new AbstractAction("Calculate")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				SingleOutputPanel.this.getController().updateModelConstants();
				SingleOutputPanel.this.calcOutputValues();
				SingleOutputPanel.this.refreshOutput();
			}
		};
	}

	private void calcOutputValues()
	{
		this.openWorker(this.getController().createWaterLevelWorker(this.getController().getOutputPrecision()));
	}

	private void refreshOutput()
	{
		this.refreshOutputLabels();
		this.refreshErrorMessage();
	}

	private void refreshOutputLabels()
	{
		BigDecimal level = this.getController().getLevel();
		BigDecimal v = this.getController().getV();
		if (level != null && v != null)
		{
			this.levelLabel.setText(level.toString());
			this.vLabel.setText(v.toString());
		}
		else
		{
			this.levelLabel.setText("");
			this.vLabel.setText("");
		}
	}

	private void refreshErrorMessage()
	{
		switch (this.getController().getError())
		{
			case CONSTANTS_NOT_SET:
				this.showErrorMessage("Constants not set!");
				break;
			case DISCHARGE_UNDERFLOW:
				this.showErrorMessage("Discharge too low!");
				break;
			case NOT_ENOUGH_DATA:
				this.showErrorMessage("Not enough data points!");
				break;
			case NONE:
			default:
				this.hideErrorMessage();
		}
	}
}
