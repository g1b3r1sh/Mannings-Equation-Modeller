package ui;

import java.util.ArrayList;
import java.util.Map.Entry;

import javax.swing.table.AbstractTableModel;

import data.DiscreteData;
import hydraulics.Pair;

/**
 * The TableModel of DiscreteData. As such, it is restricted to two columns.
**/

// TODO: Make editable
public class DiscreteDataTableModel<M extends Number, N extends Number> extends AbstractTableModel
{
	private ArrayList<Pair<M, N>> data;
	private String nameX;
	private String nameY;
	
	public DiscreteDataTableModel(DiscreteData<M, N> outsideData, String nameX, String nameY)
	{
		super();
		this.nameX = nameX;
		this.nameY = nameY;
		
		this.populate(outsideData);
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

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		return Number.class;
	}
	
	protected ArrayList<Pair<M, N>> getData()
	{
		return this.data;
	}

	private void populate(DiscreteData<M, N> outsideData)
	{
		this.data = new ArrayList<>();
		for (Entry<M, N> e : outsideData.getEntrySet())
		{
			this.data.add(new Pair<>(e.getKey(), e.getValue()));
		}
	}
}
