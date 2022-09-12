package utility;

/**
 * A pair of two objects that may be different classes (think C++ std::pair)
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
