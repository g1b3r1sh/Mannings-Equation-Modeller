package main.result;

import javax.swing.table.AbstractTableModel;

import main.result.ManningsResultModel.Result;

public class ResultsTableModel extends AbstractTableModel
{
	private static final String dischargeHeader = "<html>Q (m^3/s)<br>&nbsp;</html>";
	private static final String levelHeader = "<html>Elevation (m)<br>&nbsp;</html>";
	private static final String velocityHeader = "<html>Channel <br>Velocity (m/s)</html>";

	private Result[] results = new Result[0];

	@Override
	public int getRowCount()
	{
		return this.results.length;
	}

	@Override
	public int getColumnCount()
	{
		return 3;
	}

	@Override
	public String getColumnName(int column)
	{
		switch(column)
		{
			case 0:
				return ResultsTableModel.dischargeHeader;
			case 1:
				return ResultsTableModel.levelHeader;
			case 2:
				return ResultsTableModel.velocityHeader;
			default:
				return null;
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Result result = this.results[rowIndex];
		switch(columnIndex)
		{
			case 0:
				return result.getQ();
			case 1:
				return result.getLevel();
			case 2:
				return result.getV();
			default:
				return null;
		}
	}

	public void setData(Result[] results)
	{
		this.results = results != null ? results : new Result[0];
		this.fireTableDataChanged();
	}
}
