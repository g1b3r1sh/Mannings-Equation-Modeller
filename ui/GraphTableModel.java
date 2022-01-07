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
	
	@Override
	public void setValueAt(Object value, int rowIndex, int colIndex)
	{
		if (value instanceof BigDecimal)
		{
			BigDecimal decimal = (BigDecimal) value;
			if (colIndex == 0)
			{
				this.getData().get(rowIndex).first = this.precision.fitPrecisionX(decimal);
			}
			else
			{
				this.getData().get(rowIndex).second = this.precision.fitPrecisionY(decimal);
			}
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
