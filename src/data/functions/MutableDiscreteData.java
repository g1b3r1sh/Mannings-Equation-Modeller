package data.functions;

public interface MutableDiscreteData<M extends Number, N extends Number> extends DiscreteData<M, N>
{
	public void set(M x, N y);
	public void remove(M x);
	public void clear();
	public void load(DiscreteData<M, N> data);
}
