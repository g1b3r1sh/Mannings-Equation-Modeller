package data;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents precision of a function
**/

public class DataPrecision
{
	private int x;
	private int y;

	public DataPrecision(int precisionX, int precisionY)
	{
		this.x = precisionX;
		this.y = precisionY;
	}

	public DataPrecision(DataPrecision precision)
	{
		this(precision.getX(), precision.getY());
	}

	public void load(DataPrecision precision)
	{
		this.x = precision.getX();
		this.y = precision.getY();
	}
	
	public int getX()
	{
		return x;
	}

	public void setX(int precisionX)
	{
		this.x = precisionX;
	}
	
	public int getY()
	{
		return y;
	}

	public void setY(int precisionY)
	{
		this.y = precisionY;
	}

	// Return BigDecimal fitted to precision represented by DataPrecision
	public BigDecimal fitPrecisionX(BigDecimal decimal)
	{
		return decimal.setScale(this.x, RoundingMode.HALF_UP);
	}

	public BigDecimal fitPrecisionY(BigDecimal decimal)
	{
		return decimal.setScale(this.y, RoundingMode.HALF_UP);
	}
}
