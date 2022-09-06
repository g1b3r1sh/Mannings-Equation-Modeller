package utility;

/**
 * Stores an object of class T, allowing for storage of immutable objects by referencing the Wrapper object
**/

public class Wrapper<T>
{
	public T value;

	public Wrapper(T value)
	{
		this.value = value;
	}

	public T get()
	{
		return this.value;
	}

	public void set(T value)
	{
		this.value = value;
	}
}
