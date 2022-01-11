package data;

public class Parabola implements ContinuousFunction
{
	private int a;
	private int b;
	private int c;

	public Parabola(int a, int b, int c)
	{
		this.a = a;
		this.b = b;
		this.c = c;
	}

	@Override
	public boolean hasY(Double x)
	{
		return true;
	}

	@Override
	public Double y(Double x) throws IllegalArgumentException
	{
		return (this.a * x * x) + (this.b  * x) + this.c;
	}
}
