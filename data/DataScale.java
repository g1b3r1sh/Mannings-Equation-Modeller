package data;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents scale of a function
**/

public class DataScale
{
	private int x;
	private int y;

	public DataScale(int scaleX, int scaleY)
	{
		this.x = scaleX;
		this.y = scaleY;
	}

	public DataScale(DataScale scale)
	{
		this(scale.getX(), scale.getY());
	}

	public void load(DataScale scale)
	{
		this.x = scale.getX();
		this.y = scale.getY();
	}
	
	public int getX()
	{
		return this.x;
	}

	public void setX(int scaleX)
	{
		this.x = scaleX;
	}
	
	public int getY()
	{
		return this.y;
	}

	public void setY(int scaleY)
	{
		this.y = scaleY;
	}

	// Return BigDecimal fitted to scale represented by DataScale
	public BigDecimal fitScaleX(BigDecimal decimal)
	{
		return decimal.setScale(this.x, RoundingMode.HALF_UP);
	}

	public BigDecimal fitScaleY(BigDecimal decimal)
	{
		return decimal.setScale(this.y, RoundingMode.HALF_UP);
	}
}
