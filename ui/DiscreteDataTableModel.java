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
	
	protected ArrayList<Pair<M, N>> getData()
	{
		return this.data;
	}

	protected DiscreteData<M, N> getOutsideData()
	{
		return this.outsideData;
	}
}
