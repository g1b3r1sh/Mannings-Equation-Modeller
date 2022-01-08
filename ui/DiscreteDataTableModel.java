package ui;

import java.util.ArrayList;
import java.util.Map.Entry;

import javax.swing.table.AbstractTableModel;

import data.DiscreteData;
import hydraulics.Pair;

/**
 * The TableModel of DiscreteData. As such, it is restricted to two columns.
**/

public abstract class DiscreteDataTableModel<M extends Number, N extends Number> extends AbstractTableModel
{
	private DiscreteData<M, N> outsideData;
	private ArrayList<Pair<M, N>> data;
	private String nameX;
	private String nameY;
	
	public DiscreteDataTableModel(DiscreteData<M, N> outsideData, String nameX, String nameY)
	{
		super();

		this.outsideData = outsideData;
		this.nameX = nameX;
		this.nameY = nameY;
		
		this.data = new ArrayList<>();
		this.refresh();
	}

	@Override
	public abstract Class<?> getColumnClass(int columnIndex);
	
	public void refresh()
	{
		this.data.clear();
		for (Entry<M, N> e : this.outsideData.getEntrySet())
		{
			this.data.add(new Pair<>(e.getKey(), e.getValue()));
		}
	}

	@Override
	public int getRowCount()
	{
		return this.data.size();
	}

	@Override
	public int getColumnCount()
	{
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return columnIndex == 0 ? this.data.get(rowIndex).first : this.data.get(rowIndex).second;
	}
	
	@Override
	public String getColumnName(int column)
	{
		return column == 0 ? this.nameX : this.nameY;
	}

	// Inserts row before the current row in the index
	public void insertRow(int rowIndex)
	{
		if (this.containsRow(rowIndex))
		{
			this.data.add(rowIndex, new Pair<>(null, null));
			this.fireTableRowsInserted(rowIndex, rowIndex);
		}
	}

	public void insertRowLast()
	{
		this.data.add(new Pair<>(null, null));
		this.fireTableRowsInserted(this.data.size() - 1, this.data.size() - 1);
	}

	public void deleteRows(int rowIndex, int numRows)
	{
		if (this.containsRow(rowIndex) && numRows > 0)
		{
			for (int i = 0; i < numRows; i++)
			{
				this.data.remove(rowIndex);
			}
			this.fireTableRowsDeleted(rowIndex, rowIndex + numRows - 1);
		}
	}

	public void clear(int[] rows, int[] columns)
	{
		for (int row : rows)
		{
			for (int column : columns)
			{
				this.setValueAt(null, row, column);
			}
		}
	}

	protected ArrayList<Pair<M, N>> getData()
	{
		return this.data;
	}

	protected DiscreteData<M, N> getOutsideData()
	{
		return this.outsideData;
	}

	protected boolean containsRow(int rowIndex)
	{
		return rowIndex >= 0 && rowIndex < this.getRowCount();
	}

	protected boolean containsColumn(int columnIndex)
	{
		return columnIndex >= 0 && columnIndex < this.getColumnCount();
	}
}
