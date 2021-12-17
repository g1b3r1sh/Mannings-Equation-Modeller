package graphs;

import javax.swing.table.DefaultTableCellRenderer;

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
