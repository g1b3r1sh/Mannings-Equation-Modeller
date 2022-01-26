package main.result;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

public class OutputPanel extends JPanel
{
	private ResultScreen parent;
	private ManningsModelController controller;

	public OutputPanel(ResultScreen parent, ManningsModelController controller)
	{
		super();

		this.parent = parent;
		this.controller = controller;

		this.initPanel();
	}

	public Component getParentComponent()
	{
		return this.parent;
	}

	public ManningsModelController getController()
	{
		return this.controller;
	}

	protected <T> T runWorker(SwingWorker<T, ?> worker)
	{
		return this.parent.runWorker(worker);
	}

	protected void processResults(ManningsModelController.Result[] results)
	{
		this.parent.processResults(results);
	}

	private void initPanel()
	{
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setAlignmentX(Component.CENTER_ALIGNMENT);
	}
}
