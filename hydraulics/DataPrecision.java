package hydraulics;

public class DataPrecision
{
	private int x;
	private int y;

	public DataPrecision(int precisionX, int precisionY)
	{
		this.x = precisionX;
		this.y = precisionY;
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
}
