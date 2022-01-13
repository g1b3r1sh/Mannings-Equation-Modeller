package graphs;

public class InversePlane extends LinearPlane
{
    @Override
    public int posX(double x)
    {
        return super.posY(x);
    }

    @Override
	public int posY(double y)
    {
        return super.posX(y);
    }

    @Override
	public double inversePosX(int x)
    {
        return super.inversePosY(x);
    }

    @Override
	public double inversePosY(int y)
    {
        return super.inversePosX(y);
    }
}