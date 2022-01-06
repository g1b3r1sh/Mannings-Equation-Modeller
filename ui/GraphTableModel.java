package ui;

import java.math.BigDecimal;

import data.DataPrecision;
import data.DiscreteData;

/**
 * GraphTableModel that takes in BigDecimal as data
 * Overrides setValueAt method to allow for data to be changed
**/

public class GraphTableModel extends DiscreteDataTableModel<BigDecimal, BigDecimal>
{
	private DataPrecision precision;

	public GraphTableModel(DiscreteData<BigDecimal, BigDecimal> outsideData, DataPrecision precision, String nameX, String nameY)
	{
		super(outsideData, nameX, nameY);
		this.precision = precision;
	}
	
	// TODO: Use events to update data instead
	@Override
	public void setValueAt(Object value, int rowIndex, int colIndex)
	{
		if (value instanceof Number)
		{
			BigDecimal decimal = new BigDecimal((((Number) value).doubleValue()));
			if (colIndex == 0)
			{
				// To replace x data, remove x from Data object and add new x to it
				this.getOutsideData().remove(this.getData().get(rowIndex).first);
				BigDecimal newX = this.precision.fitPrecisionX(decimal);
				BigDecimal y = this.getData().get(rowIndex).second;
				this.getOutsideData().set(newX, y);
			}
			else
			{
				// To replace y data, simply set the y value in data
				BigDecimal x = this.getData().get(rowIndex).first;
				BigDecimal newY = this.precision.fitPrecisionY(decimal);
				this.getOutsideData().set(x, newY);
			}
			this.refresh();
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		return BigDecimal.class;
	}

	public DataPrecision getPrecision()
	{
		return this.precision;
	} 
}
