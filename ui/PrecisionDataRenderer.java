package ui;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * Handles rendering of Numbers with specified precision
**/

public class PrecisionDataRenderer extends DefaultTableCellRenderer
{
	private int precision;

	public PrecisionDataRenderer(int precision)
	{
		super();
		this.precision = precision;
	}

	@Override
	public void setValue(Object value)
	{
		if (value instanceof Number)
		{
			this.setText(String.format("%." + this.precision + "f", ((Number) value).doubleValue()));
		}
		else
		{
			this.setText("");
		}
	}
}
