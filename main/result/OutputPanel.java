package main.result;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

public class OutputPanel extends JPanel
{
	private ResultScreen parent;
	private ManningsResultModel resultModel;

	public OutputPanel(ResultScreen parent, ManningsResultModel controller)
	{
		super();

		this.parent = parent;
		this.resultModel = controller;

		this.initPanel();
	}

	public Component getParentComponent()
	{
		return this.parent;
	}

	public ManningsResultModel getController()
	{
		return this.resultModel;
	}

	protected <T> T runWorker(SwingWorker<T, ?> worker)
	{
		return this.parent.runWorker(worker);
	}

	protected void processResults(ManningsResultModel.Result[] results)
	{
		this.parent.processResults(results);
	}

	private void initPanel()
	{
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setAlignmentX(Component.CENTER_ALIGNMENT);
	}
}
