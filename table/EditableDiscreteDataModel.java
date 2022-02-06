package table;

import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;

import data.DataScale;
import data.functions.DiscreteData;
import utility.Pair;

/**
 * GraphTableModel that takes in BigDecimal as data
 * Overrides setValueAt method to allow for data to be changed
**/

public class EditableDiscreteDataModel extends DiscreteDataTableModel<BigDecimal, BigDecimal>
{
	private DataScale scale;

	public EditableDiscreteDataModel(DiscreteData<BigDecimal, BigDecimal> outsideData, DataScale outsideScale, String nameX, String nameY)
	{
		super(outsideData, nameX, nameY);
		this.scale = new DataScale(outsideScale);
		this.scale.addPropertyChangeListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		super.propertyChange(evt);
		if (evt.getSource() == this.scale)
		{
			if ("x".equals(evt.getPropertyName()))
			{
				this.updateXScale();
			}
			else if ("y".equals(evt.getPropertyName()))
			{
				this.updateYScale();
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
					newValue = this.scale.fitScaleX(newValue);
					oldValue = row.first;
					row.first = newValue;
				}
				else
				{
					newValue = this.scale.fitScaleY(newValue);
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

	public DataScale getScale()
	{
		return this.scale;
	}

	private void updateXScale()
	{
		for (Pair<BigDecimal, BigDecimal> pair : this.getData())
		{
			if (pair.first != null)
			{
				pair.first = this.scale.fitScaleX(pair.first);
			}
		}
		this.fireTableRowsUpdated(0, this.getData().size() - 1);
	}

	private void updateYScale()
	{
		for (Pair<BigDecimal, BigDecimal> pair : this.getData())
		{
			if (pair.second != null)
			{
				pair.second = this.scale.fitScaleY(pair.second);
			}
		}
		this.fireTableRowsUpdated(0, this.getData().size() - 1);
	}
}
