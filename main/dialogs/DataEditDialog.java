package main.dialogs;

import java.awt.Frame;

import javax.swing.JOptionPane;

public class DataEditDialog extends EditDialog
{
	private static final String TITLE = "Edit Data";

	private DataEditScreen screen;

	public DataEditDialog(Frame parentWindow, DataEditScreen screen)
	{
		super(parentWindow, DataEditDialog.TITLE, screen);
		
		this.screen = screen;
	}

	public DataEditScreen getEditScreen()
	{
		return this.screen;
	}

	@Override
	protected void prepareScreen()
	{
		this.screen.refresh();
	}

	@Override
	protected boolean canSave() {
		if (this.screen.getModel().containsDuplicates())
		{
			JOptionPane.showMessageDialog(this, "Error: X column contains duplicates.", "Error: Duplicates", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (this.screen.getModel().containsEmpty())
		{
			int result = JOptionPane.showConfirmDialog(this, "Warning: Some cells are blank. They will not be included when updating the data. Continue?", "Warning: Blanks", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.NO_OPTION || result == JOptionPane.CLOSED_OPTION)
			{
				return false;
			}
		}
		return true;
	}

	@Override
	protected void save()
	{
		this.firePropertyChange("update", null, this.screen.getModel());
	}
}
