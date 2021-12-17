package ui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import java.awt.CardLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class ScreenSwitcher extends JPanel implements ActionListener
{
	private JPanel cards;
	private CardLayout cardLayout;
	private JButton prev;
	private JButton next;
	private int index;
	private ArrayList<String> names;

	public ScreenSwitcher(JComponent initScreen, String name)
	{
		this.setLayout(new BorderLayout());

		this.names = new ArrayList<>();
		this.cardLayout = new CardLayout();
		this.cards = new JPanel(this.cardLayout);

		this.prev = new JButton("Prev");
		this.prev.setActionCommand("prev");
		this.prev.addActionListener(this);
		this.next = new JButton("Next");
		this.next.setActionCommand("next");
		this.next.addActionListener(this);
		
		this.addScreen(initScreen, name);
		this.index = 0;
		this.add(this.cards);

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
		this.names.add(name);
		this.refreshButtons();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("next"))
		{
			this.cardLayout.next(this.cards);
			this.index++;
		}
		else if (e.getActionCommand().equals("prev"))
		{
			this.cardLayout.previous(this.cards);
			this.index--;
		}
		this.refreshButtons();
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
