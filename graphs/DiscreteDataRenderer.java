package graphs;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * Handles rendering of Numbers with specified precision
**/

// TODO: Rename file to PrecisionNumberRenderer and move to ui package
// TODO: Use the term scale instead of precision
public class DiscreteDataRenderer extends DefaultTableCellRenderer
{
	private int precision;

	public DiscreteDataRenderer(int precision)
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
