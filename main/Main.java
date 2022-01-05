package main;

import javax.swing.SwingUtilities;

/**
 * Main program
 * Constructs and launches main window of application
**/

public class Main
{
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				final MainWindow window = new MainWindow(new CrossSectionModel());
				window.setVisible(true);
			}
		});
	}
}