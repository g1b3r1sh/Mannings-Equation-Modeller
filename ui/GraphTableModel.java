package ui;

import java.math.BigDecimal;

import data.DataPrecision;
import data.functions.DiscreteData;
import utility.Pair;

/**
 * GraphTableModel that takes in BigDecimal as data
 * Overrides setValueAt method to allow for data to be changed
**/

public class GraphTableModel extends DiscreteDataTableModel<BigDecimal, BigDecimal>
{
	private DataPrecision precision;
	private DataPrecision outsidePrecision;

	public GraphTableModel(DiscreteData<BigDecimal, BigDecimal> outsideData, DataPrecision outsidePrecision, String nameX, String nameY)
	{
		super(outsideData, nameX, nameY);
		this.outsidePrecision = outsidePrecision;
		this.precision = new DataPrecision(this.outsidePrecision);
	}

	@Override
	public void refresh()
	{
		super.refresh();
		if (this.precision != null)
		{
			if (this.precision.getX() != this.outsidePrecision.getX())
			{
				this.precision.setX(this.outsidePrecision.getX());
				this.updatePrecisionX();
			}
			if (this.precision.getY() != this.outsidePrecision.getY())
			{
				this.precision.setY(this.outsidePrecision.getY());
				this.updatePrecisionY();
			}
		}
	}
	
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex)
	{
		if (this.containsRow(rowIndex) && this.containsColumn(columnIndex))
		{
			Pair<BigDecimal, BigDecimal> row = this.getData().get(rowIndex);
			if (value == null)
			{
				boolean changed = false;
				if (columnIndex == 0 && row.first != null)
				{
					changed = true;
					row.first = null;
				}
				else if (row.second != null)
				{
					changed = true;
					row.second = null;
				}
				if (changed)
				{
					this.fireTableCellUpdated(rowIndex, columnIndex);
				}
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

	// Replaces data
	public void pasteData(BigDecimal[][] data, int rowIndex, int columnIndex)
	{
		if (this.containsRow(rowIndex) && this.containsColumn(columnIndex) && data.length > 0 && data[0].length > 0)
		{
			for (int y = 0; y < data.length; y++)
			{
				int pasteRowIndex = y + rowIndex;
				if (pasteRowIndex >= this.getData().size())
				{
					this.getData().add(new Pair<>(null, null));
				}
				if (columnIndex == 0)
				{
					this.setValueAt(data[y][0], pasteRowIndex, 0);
					this.setValueAt(data[y][1], pasteRowIndex, 1);
				}
				else if (columnIndex == 1)
				{
					this.setValueAt(data[y][0], pasteRowIndex, 1);
				}
			}
			this.fireTableDataChanged();
		}
	}

	public DataPrecision getPrecision()
	{
		return this.precision;
	}

	public void updatePrecisionX()
	{
		for (Pair<BigDecimal, BigDecimal> pair : this.getData())
		{
			if (pair.first != null)
			{
				pair.first = this.precision.fitPrecisionX(pair.first);
			}
		}
		this.fireTableRowsUpdated(0, this.getData().size() - 1);
	}

	public void updatePrecisionY()
	{
		for (Pair<BigDecimal, BigDecimal> pair : this.getData())
		{
			if (pair.second != null)
			{
				pair.second = this.precision.fitPrecisionY(pair.second);
			}
		}
		this.fireTableRowsUpdated(0, this.getData().size() - 1);
	}
}
