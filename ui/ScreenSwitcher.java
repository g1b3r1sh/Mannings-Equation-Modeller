package ui;

import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ScreenSwitcher extends JPanel implements ActionListener
{
	private JButton prev;
	private JButton next;
	private ArrayList<JComponent> screens;
	private JComponent currScreen;

	public ScreenSwitcher(JComponent initScreen)
	{
		this.setLayout(new BorderLayout());
		this.setBackground(Color.GRAY);

		this.prev = new JButton("Prev");
		this.prev.setActionCommand("prev");
		this.prev.addActionListener(this);
		this.next = new JButton("Next");
		this.next.setActionCommand("next");
		this.next.addActionListener(this);
		this.screens = new ArrayList<>();

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(prev);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(next);
		this.add(buttonPanel, BorderLayout.SOUTH);

		this.addScreen(initScreen);
		this.switchScreen(0);
	}

	public void addScreen(JComponent screen)
	{
		this.screens.add(screen);
		this.refreshButtonVisibility();
	}

	public void prevScreen()
	{
		if (this.currIndex() > 0)
		{
			this.switchScreen(this.currIndex() - 1);
		}
	}

	public void nextScreen()
	{
		if (this.currIndex() < this.screens.size() - 1)
		{
			this.switchScreen(this.currIndex() + 1);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("next"))
		{
			this.nextScreen();
		}
		else if (e.getActionCommand().equals("prev"))
		{
			this.prevScreen();
		}
	}

	private void switchScreen(int index)
	{
		JComponent screen = this.screens.get(index);
		this.add(screen, BorderLayout.CENTER);
		this.currScreen = screen;
		this.refreshButtonVisibility();
	}

	private int currIndex()
	{
		return this.screens.indexOf(this.currScreen);
	}

	private void refreshButtonVisibility()
	{
		if (this.currIndex() == 0)
		{
			prev.setVisible(false);
		}
		else
		{
			prev.setVisible(true);
		}

		if (this.currIndex() == this.screens.size() - 1)
		{
			next.setVisible(false);
		}
		else
		{
			next.setVisible(true);
		}
	}
}
