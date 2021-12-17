package graphs;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.table.AbstractTableModel;

import hydraulics.Pair;

/**
 * The TableModel of the data of a graph. As such, it is restricted to two columns.
**/

// TODO: Make editable
public class GraphTableModel <N extends Number, M extends Number> extends AbstractTableModel
{
	private ArrayList<Pair<N, M>> data;
	private String nameX;
	private String nameY;
	
	public GraphTableModel(DiscreteData<N, M> outsideData, String nameX, String nameY)
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
	
	protected ArrayList<Pair<N, M>> getData()
	{
		return this.data;
	}

	private void populate(DiscreteData<N, M> data)
	{
		this.data = new ArrayList<>();
		Iterator<N> it = data.getXSet().iterator();
		for (int i = 0; i < data.size(); i++)
		{
			N x = it.next();
			this.data.add(new Pair<>(x, data.y(x)));
		}
	}
}
