package data;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents scale of a function
**/

public class DataScale
{
	private int x;
	private int y;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public DataScale(int scaleX, int scaleY)
	{
		this.x = scaleX;
		this.y = scaleY;
	}

	public DataScale(DataScale scale)
	{
		this(scale.getX(), scale.getY());
	}

	public void load(DataScale scale)
	{
		this.setX(scale.getX());
		this.setY(scale.getY());
	}

	public int getX()
	{
		return this.x;
	}

	public void setX(int scaleX)
	{
		if (this.x != scaleX)
		{
			int oldX = this.x;
			this.x = scaleX;
			this.propertyChangeSupport.firePropertyChange("x", oldX, this.x);
		}
	}

	public int getY()
	{
		return this.y;
	}

	public void setY(int scaleY)
	{
		if (this.y != scaleY)
		{
			int oldY = this.y;
			this.y = scaleY;
			this.propertyChangeSupport.firePropertyChange("y", oldY, this.y);
		}
	}

	// Return BigDecimal fitted to scale represented by DataScale
	public BigDecimal fitScaleX(BigDecimal decimal)
	{
		return decimal.setScale(this.x, RoundingMode.HALF_UP);
	}

	public BigDecimal fitScaleY(BigDecimal decimal)
	{
		return decimal.setScale(this.y, RoundingMode.HALF_UP);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		this.propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		this.propertyChangeSupport.removePropertyChangeListener(listener);
	}
}
