package main.inputscreen;

import java.awt.Frame;

import ui.EditDialog;

public class GraphEditDialog extends EditDialog
{
	private static final String TITLE = "Edit Graph";

	private GraphEditScreen screen;

	public GraphEditDialog(Frame parentWindow, GraphEditScreen screen)
	{
		super(parentWindow, GraphEditDialog.TITLE, screen);
		
		this.screen = screen;
	}

	@Override
	protected void prepareScreen()
	{
		this.screen.refresh();
	}

	@Override
	protected boolean canOkClose()
	{
		return true;
	}

	@Override
	protected void okCloseActions()
	{
		this.firePropertyChange("update", null, this.screen.getPreviewGraph());
	}
}
