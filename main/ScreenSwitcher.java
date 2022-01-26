package main;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.CardLayout;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;

/**
 * Component that allows for switching of screens controlled by button menu at bottom of screen.
**/

public class ScreenSwitcher extends JPanel
{
	private JPanel cards;
	private CardLayout cardLayout;
	private JButton prev;
	private JButton next;
	private JLabel label;
	private int index;
	private ArrayList<String> names;

	public ScreenSwitcher()
	{
		this.setLayout(new BorderLayout());

		this.names = new ArrayList<>();
		this.cardLayout = new CardLayout();
		this.cards = new JPanel(this.cardLayout);

		this.prev = new JButton(this.createPrevAction());
		this.next = new JButton(this.createNextAction());
		this.label = this.createLabel();
		
		this.index = 0;
		this.add(this.cards);

		this.add(this.createButtonPanel(), BorderLayout.NORTH);
	}

	public ScreenSwitcher(JComponent initScreen, String name)
	{
		super();
		this.addScreen(initScreen, name);
	}

	public void addScreen(JComponent screen, String name)
	{
		this.cards.add(screen, name);
		this.names.add(name);
		this.refreshButtons();
	}

	public void switchScreen(int index)
	{
			this.cardLayout.show(this.cards, this.names.get(index));
			this.index = index;
			this.refreshButtons();
	}

	public Action createNextAction()
	{
		return new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ScreenSwitcher.this.cardLayout.next(ScreenSwitcher.this.cards);
				ScreenSwitcher.this.index++;
				ScreenSwitcher.this.refreshButtons();
			}
		};
	}

	public Action createPrevAction()
	{
		return new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ScreenSwitcher.this.cardLayout.previous(ScreenSwitcher.this.cards);
				ScreenSwitcher.this.index--;
				ScreenSwitcher.this.refreshButtons();
			}
		};
	}

	private void refreshButtons()
	{
		// Update visibility
		if (this.index == 0)
		{
			this.prev.setVisible(false);
		}
		else
		{
			this.prev.setVisible(true);
		}
		if (this.index == this.cards.getComponentCount() - 1)
		{
			this.next.setVisible(false);
		}
		else
		{
			this.next.setVisible(true);
		}

		// Update names
		this.label.setText(this.names.get(this.index));
		if (this.prev.isVisible())
		{
			this.prev.setText(this.names.get(this.index - 1));
		}
		if (this.next.isVisible())
		{
			this.next.setText(this.names.get(this.index + 1));
		}
	}

	private JPanel createButtonPanel()
	{
		JPanel buttonPanel = new JPanel(new GridLayout(1, 3));

		JPanel prevPanel = new JPanel();
		prevPanel.setLayout(new BoxLayout(prevPanel, BoxLayout.X_AXIS));
		prevPanel.add(this.prev);
		prevPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(prevPanel);

		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
		labelPanel.add(Box.createHorizontalGlue());
		labelPanel.add(this.label);
		labelPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(labelPanel);

		JPanel nextPanel = new JPanel();
		nextPanel.setLayout(new BoxLayout(nextPanel, BoxLayout.X_AXIS));
		nextPanel.add(Box.createHorizontalGlue());
		nextPanel.add(this.next);
		buttonPanel.add(nextPanel);

		return buttonPanel;
	}

	private JLabel createLabel()
	{
		JLabel label = new JLabel();
		label.setFont(label.getFont().deriveFont(20f));
		return label;
	}
}
