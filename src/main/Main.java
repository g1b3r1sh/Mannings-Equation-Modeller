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
				final CrossSectionDataset model = new CrossSectionDataset();
				final MainWindow window = new MainWindow(model);
				window.setVisible(true);
			}
		});
	}
}