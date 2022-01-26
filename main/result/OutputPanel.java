package main.result;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

public class OutputPanel extends JPanel
{
	private ResultScreen parent;
	private ResultScreenController controller;

	public OutputPanel(ResultScreen parent, ResultScreenController controller)
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

	public ResultScreenController getController()
	{
		return this.controller;
	}

	protected void openWorker(SwingWorker<?, ?> worker)
	{
		this.parent.openWorker(worker);
	}

	protected void processResults(ResultScreenController.Result[] results)
	{
		this.parent.processResults(results);
	}

	private void initPanel()
	{
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setAlignmentX(Component.CENTER_ALIGNMENT);
	}
}
