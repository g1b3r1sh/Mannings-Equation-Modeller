package ui;

import java.math.BigDecimal;
import java.math.RoundingMode;

import data.DataPrecision;
import data.DiscreteData;

/**
 * GraphTableModel that takes in BigDecimal as data
 * Overrides setValueAt method to allow for data to be changed
**/

public class BigDecimalGraphTableModel extends DiscreteDataTableModel<BigDecimal, BigDecimal>
{
	private DataPrecision precision;

	public BigDecimalGraphTableModel(DiscreteData<BigDecimal, BigDecimal> outsideData, DataPrecision precision, String nameX, String nameY)
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
				BigDecimal newX = decimal.setScale(this.precision.getX(), RoundingMode.HALF_UP);
				BigDecimal y = this.getData().get(rowIndex).second;
				this.getData().get(rowIndex).first = newX;
				this.getOutsideData().set(newX, y);
			}
			else
			{
				// To replace y data, simply set the y value in data
				BigDecimal x = this.getData().get(rowIndex).first;
				BigDecimal newY = decimal.setScale(this.precision.getY(), RoundingMode.HALF_UP);
				this.getData().get(rowIndex).second = newY;
				this.getOutsideData().set(x, newY);
			}
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		return BigDecimal.class;
	}
}
