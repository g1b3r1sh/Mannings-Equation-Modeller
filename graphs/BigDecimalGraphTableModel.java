package graphs;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalGraphTableModel extends GraphTableModel<BigDecimal, BigDecimal>
{
	DiscreteData<BigDecimal, BigDecimal> outsideData;

	public BigDecimalGraphTableModel(DiscreteData<BigDecimal, BigDecimal> outsideData, String nameX, String nameY)
	{
		super(outsideData, nameX, nameY);
		this.outsideData = outsideData;
	}
	
	@Override
	public void setValueAt(Object value, int rowIndex, int colIndex)
	{
		if (value instanceof Number)
		{
			BigDecimal decimal = new BigDecimal((((Number) value).doubleValue()));
			if (colIndex == 0)
			{
				this.outsideData.remove(this.getData().get(rowIndex).first);
				BigDecimal x = decimal.setScale(this.outsideData.getPrecisionX(), RoundingMode.HALF_UP);
				BigDecimal y = this.getData().get(rowIndex).second;
				this.getData().get(rowIndex).first = x;
				this.outsideData.set(x, y);
			}
			else
			{
				BigDecimal x = this.getData().get(rowIndex).first;
				BigDecimal y = decimal.setScale(this.outsideData.getPrecisionY(), RoundingMode.HALF_UP);
				this.getData().get(rowIndex).second = y;
				this.outsideData.set(x, y);
			}
		}
	}
}
