package hydraulics;

/**
 * A pair of two objects that can be different classes
**/

public class Pair<F, S>
{
	public F first;
	public S second;
	
	public Pair(F first, S second)
	{
		this.first = first;
		this.second = second;
	}
}
