package main.dialogs;

import java.awt.Frame;

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
		this.screen.reset();
	}

	@Override
	protected boolean canSave()
	{
		return true;
	}

	@Override
	protected void save()
	{
		this.firePropertyChange("update", null, this.screen.getPreviewGraph());
	}
}
