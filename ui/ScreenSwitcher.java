package ui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import java.awt.CardLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ScreenSwitcher extends JPanel implements ActionListener
{
	private JPanel cards;
	private CardLayout cardLayout;
	private JButton prev;
	private JButton next;
	private int cardIndex;

	public ScreenSwitcher(JComponent initScreen, String name)
	{
		this.setLayout(new BorderLayout());

		this.cardLayout = new CardLayout();
		this.cards = new JPanel(this.cardLayout);
		this.cards.add(initScreen, name);
		this.cardIndex = 0;
		this.add(this.cards);

		this.prev = new JButton("Prev");
		this.prev.setActionCommand("prev");
		this.prev.addActionListener(this);
		this.next = new JButton("Next");
		this.next.setActionCommand("next");
		this.next.addActionListener(this);

		JPanel buttonPanel = new JPanel();
		this.add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(prev);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(next);
	}

	public void addScreen(JComponent screen, String name)
	{
		this.cards.add(screen, name);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("next"))
		{
			this.cardLayout.next(this.cards);
		}
		else if (e.getActionCommand().equals("prev"))
		{
			this.cardLayout.previous(this.cards);
		}
	}


}
