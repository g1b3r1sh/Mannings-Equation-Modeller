package data.functions;

import java.util.Map.Entry;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * An implementation of DiscreteData using TreeMap
**/

public class MapDiscreteData<M extends Number, N extends Number> implements MutableDiscreteData<M, N>
{
	private NavigableMap<M, N> data = new TreeMap<>();
	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

	public MapDiscreteData() { }

	public MapDiscreteData(DiscreteData<M, N> data)
	{
		this.load(data);
	}

	@Override
	public boolean hasY(M x)
	{
		return this.data.containsKey(x);
	}

	@Override
	public N y(M x)
	{
		return this.data.get(x);
	}

	@Override
	public void set(M x, N y)
	{
		this.data.put(x, y);
		this.fireDataChange();
	}

	@Override
	public void remove(M x)
	{
		this.data.remove(x);
		this.fireDataChange();
	}

	@Override
	public void clear()
	{
		this.data.clear();
		this.fireDataChange();
	}

	@Override
	public void load(DiscreteData<M, N> data)
	{
		this.clear();
		for (M x : data.getXSet())
		{
			this.data.put(x, data.y(x));
		}
		this.fireDataChange();
	}

	@Override
	public int size()
	{
		return this.data.size();
	}

	// Note: Any modifications to sets will also modify data
	@Override
	public NavigableSet<M> getXSet()
	{
		return new TreeSet<M>(this.data.keySet());
	}

	@Override
	public Set<Entry<M, N>> getEntrySet()
	{
		return this.data.entrySet();
	}

	@Override
	public void addPropertyChangeListener​(PropertyChangeListener listener)
	{
		this.changeSupport.addPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener​(PropertyChangeListener listener)
	{
		this.changeSupport.removePropertyChangeListener(listener);
	}

	private void fireDataChange()
	{
		this.changeSupport.firePropertyChange("update", null, null);
	}
}