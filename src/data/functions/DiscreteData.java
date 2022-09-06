package data.functions;

import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;

/**
 * Represents a set of discrete data coordinates
**/

public interface DiscreteData<M extends Number, N extends Number> extends Function<M, N>
{
	public int size();
	public NavigableSet<M> getXSet();
	public Set<Map.Entry<M, N>> getEntrySet();
	public void addPropertyChangeListener​(PropertyChangeListener listener);
	public void removePropertyChangeListener​(PropertyChangeListener listener);
}