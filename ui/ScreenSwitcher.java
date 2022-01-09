package ui;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import java.awt.CardLayout;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

/**
 * Component that allows for switching of screens controlled by button menu at bottom of screen.
**/

public class ScreenSwitcher extends JPanel
{
	private JPanel cards;
	private CardLayout cardLayout;
	private JButton prev;
	private JButton next;
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
		
		this.index = 0;
		this.add(this.cards);

		JPanel buttonPanel = new JPanel();
		this.add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(prev);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(next);
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

	public void switchScreen(String name)
	{
		if (this.names.contains(name))
		{
			this.cardLayout.show(this.cards, name);
			this.index = this.names.indexOf(name);
			this.refreshButtons();
		}
	}

	public void switchFirst()
	{
		this.cardLayout.first(this.cards);
		this.index = 0;
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
			prev.setVisible(false);
		}
		else
		{
			prev.setVisible(true);
		}
		if (this.index == this.cards.getComponentCount() - 1)
		{
			next.setVisible(false);
		}
		else
		{
			next.setVisible(true);
		}

		// Update names
		if (this.prev.isVisible())
		{
			this.prev.setText(this.names.get(this.index - 1));
		}
		if (this.next.isVisible())
		{
			this.next.setText(this.names.get(this.index + 1));
		}
	}
}
