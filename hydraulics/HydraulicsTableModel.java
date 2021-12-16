package hydraulics;

import java.util.Iterator;

import javax.swing.table.AbstractTableModel;

import graphs.DiscreteData;

// TODO: Make editable
public class HydraulicsTableModel <N extends Number, M extends Number> extends AbstractTableModel
{
	private Number[][] displayData;
	private int numRows;
	private int precisionX;
	private int precisionY;
	
	public HydraulicsTableModel(DiscreteData<N, M> data, int precisionX, int precisionY)
	{
		super();
		this.displayData = new Number[0][0];
		this.numRows = 0;
		this.precisionX = 0;
		this.precisionY = 0;
		
		this.populate(data, precisionX, precisionY);
	}

	@Override
	public int getRowCount()
	{
		return this.numRows;
	}

	@Override
	public int getColumnCount()
	{
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return String.format("%." + (columnIndex == 0 ? this.precisionX : this.precisionY) + "f", displayData[rowIndex][columnIndex].doubleValue());
	}
	
	@Override
	public String getColumnName(int column)
	{
		return column == 0 ? "X" : "Y";
	}
	
	private void populate(DiscreteData<N, M> data, int precisionX, int precisionY)
	{
		this.displayData = new Number[data.getXSet().size()][2];
		this.numRows = data.getXSet().size();
		this.precisionX = precisionX;
		this.precisionY = precisionY;
		Iterator<N> it = data.getXSet().iterator();
		for (int i = 0; i < data.getXSet().size(); i++)
		{
			N x = it.next();
			this.displayData[i][0] = x;
			this.displayData[i][1] = data.y(x);
		}
	}
}
