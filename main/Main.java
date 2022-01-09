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
				final CrossSectionModel model = new CrossSectionModel();
				final MainWindow window = new MainWindow(model);
				// Link controller to model and window
				final CrossSectionController controller = new CrossSectionController(model, window);
				window.setVisible(true);
			}
		});
	}
}