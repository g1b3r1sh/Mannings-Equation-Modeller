package hydraulics;

public class DataPrecision
{
	private int precisionX;
	private int precisionY;

	public DataPrecision(int precisionX, int precisionY)
	{
		this.precisionX = precisionX;
		this.precisionY = precisionY;
	}
	
	public int getPrecisionX()
	{
		return precisionX;
	}

	public void setPrecisionX(int precisionX)
	{
		this.precisionX = precisionX;
	}
	
	public int getPrecisionY()
	{
		return precisionY;
	}

	public void setPrecisionY(int precisionY)
	{
		this.precisionY = precisionY;
	}
}
