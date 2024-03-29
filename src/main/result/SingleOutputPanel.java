package main.result;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.result.ManningsResultModel.Result;
import main.result.ManningsResultModel.ModelError;

public class SingleOutputPanel extends OutputPanel
{
	private JLabel errorLabel = SingleOutputPanel.createErrorLabel();
	private JTextField levelLabel = SingleOutputPanel.createOutputField();
	private JTextField vLabel = SingleOutputPanel.createOutputField();

	public SingleOutputPanel(ResultScreen parent, ManningsResultModel resultModel)
	{
		super(parent, resultModel);
		this.addComponents();
	}

	/// Panel methods

	private static JLabel createErrorLabel()
	{
		JLabel errorLabel = new JLabel("");
		errorLabel.setForeground(Color.RED);
		errorLabel.setVisible(false);
		return errorLabel;
	}

	private static JTextField createOutputField()
	{
		return new JTextField(10)
		{
			@Override
			public Dimension getMaximumSize()
			{
				return this.getPreferredSize();
			}
		};
	}

	private void addComponents()
	{
		this.add(ResultScreen.createHeader("Discharge Input"));
		this.add(ResultScreen.sideSeperator());
		this.add(ResultScreen.sidePadding());
		this.add(this.inputPanel());
		this.add(ResultScreen.sidePadding());
		this.add(ResultScreen.sideButton(this.calculateAction()));
		this.add(ResultScreen.sidePadding());
		this.add(ResultScreen.createHeader("Output"));
		this.add(ResultScreen.sideSeperator());
		this.add(ResultScreen.sidePadding());
		this.add(this.outputPanel());
	}

	private JPanel inputPanel()
	{
		JPanel panel = ResultScreen.mainSidePanel();
		panel.add(ResultScreen.numberEditPanel(this.getParentComponent(), "Cross-Section Discharge (m^3/s)", this.getModel().getQ()));
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
				Result result = SingleOutputPanel.this.calcResult();
				if (result != null)
				{
					SingleOutputPanel.this.setOutput(result);
				}
			}
		};
	}

	private Result calcResult()
	{
		this.getModel().updateModelConstants();
		return this.runWorker(this.getModel().createWaterLevelWorker(this.getModel().getOutputScale().value));
	}

	private void setOutput(Result result)
	{
		this.setOutputLabels(result);
		this.showErrorMessage(result.getError());
	}

	private void setOutputLabels(Result result)
	{
		BigDecimal level = result.getLevel();
		BigDecimal v = result.getV();
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

	private void showErrorMessage(ModelError error)
	{
		switch (error)
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
