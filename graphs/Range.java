package graphs;

/**
 * Represents numerical range
**/

public class Range
{
	private int lower;
	private int upper;

	public Range(int lower, int upper)
	{
		this.lower = lower;
		this.setUpper(upper);
	}

	public Range(Range range)
	{
		this(range.getLower(), range.getUpper());
	}

	public void copy(Range range)
	{
		this.lower = range.lower;
		this.upper = range.upper;
	}

	public int getLower()
	{
		return this.lower;
	}

	public int getUpper()
	{
		return this.upper;
	}

	public void setLower(int lower)
	{
		if (lower < this.upper)
		{
			this.lower = lower;
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}

	public void setUpper(int upper)
	{
		if (upper > this.lower)
		{
			this.upper = upper;
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}
	
	public double getNumber(double fraction)
	{
		return this.size() * fraction + this.lower;
	}
	
	public int size()
	{
		return this.getUpper() - this.getLower();
	}
}