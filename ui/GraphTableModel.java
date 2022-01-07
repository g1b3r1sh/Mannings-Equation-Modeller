package ui;

import hydraulics.Pair;

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
	public void setValueAt(Object value, int rowIndex, int columnIndex)
	{
		if (this.containsRow(rowIndex) && this.containsColumn(columnIndex))
		{
			Pair<BigDecimal, BigDecimal> row = this.getData().get(rowIndex);
			if (value == null)
			{
				if (columnIndex == 0)
				{
					row.first = null;
				}
				else
				{
					row.second = null;
				}
				this.fireTableCellUpdated(rowIndex, columnIndex);
			}
			else if (value instanceof BigDecimal)
			{
				BigDecimal newValue = (BigDecimal) value;
				BigDecimal oldValue;
				if (columnIndex == 0)
				{
					newValue = this.precision.fitPrecisionX(newValue);
					oldValue = row.first;
					row.first = newValue;
				}
				else
				{
					newValue = this.precision.fitPrecisionY(newValue);
					oldValue = row.second;
					row.second = newValue;
				}
				if (!newValue.equals(oldValue))
				{
					this.fireTableCellUpdated(rowIndex, columnIndex);
				}
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
