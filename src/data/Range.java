package data;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Represents numerical range
**/

public class Range
{
	private int lower;
	private int upper;
	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

	public Range(int lower, int upper)
	{
		this.lower = lower;
		this.setUpper(upper);
	}

	public Range(Range range)
	{
		this(range.lower, range.upper);
	}

	public void copy(Range range)
	{
		// Must copy so that this is not an invalid range when the first value is copied
		if (range.getLower() >= this.getUpper())
		{
			this.setUpper(range.getUpper());
			this.setLower(range.getLower());
		}
		else
		{
			this.setLower(range.getLower());
			this.setUpper(range.getUpper());
		}
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
			int oldLower = this.lower;
			this.lower = lower;
			this.changeSupport.firePropertyChange("lower", oldLower, lower);
		}
		else
		{
			throw new IllegalArgumentException("Lower cannot be greater than or equal to upper.");
		}
	}

	public void setUpper(int upper)
	{
		if (upper > this.lower)
		{
			int oldUpper = this.upper;
			this.upper = upper;
			this.changeSupport.firePropertyChange("upper", oldUpper, upper);
		}
		else
		{
			throw new IllegalArgumentException("Upper cannot be less than or equal to lower.");
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

	public void addPropertyChangeListener​(PropertyChangeListener listener)
	{
		this.changeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener​(PropertyChangeListener listener)
	{
		this.changeSupport.removePropertyChangeListener(listener);
	}

	public static class Double
	{
		private double lower;
		private double upper;
		private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

		public Double(double lower, double upper)
		{
			this.lower = lower;
			this.setUpper(upper);
		}

		public Double(Range.Double range)
		{
			this(range.lower, range.upper);
		}

		public void copy(Range.Double range)
		{
			if (range.getLower() > this.getUpper())
			{
				this.setUpper(range.getUpper());
				this.setLower(range.getLower());
			}
			else
			{
				this.setLower(range.getLower());
				this.setUpper(range.getUpper());
			}
		}

		public double getLower()
		{
			return this.lower;
		}

		public double getUpper()
		{
			return this.upper;
		}

		public void setLower(double lower)
		{
			if (lower <= this.upper)
			{
				double oldLower = this.lower;
				this.lower = lower;
				this.changeSupport.firePropertyChange("lower", oldLower, lower);
			}
			else
			{
				throw new IllegalArgumentException("Lower cannot be greater than upper.");
			}
		}

		public void setUpper(double upper)
		{
			if (upper >= this.lower)
			{
				double oldUpper = this.upper;
				this.upper = upper;
				this.changeSupport.firePropertyChange("upper", oldUpper, upper);
			}
			else
			{
				throw new IllegalArgumentException("Upper cannot be less than lower.");
			}
		}

		public double getNumber(double fraction)
		{
			return this.size() * fraction + this.lower;
		}

		public double size()
		{
			return this.getUpper() - this.getLower();
		}

		public void addPropertyChangeListener​(PropertyChangeListener listener)
		{
			this.changeSupport.addPropertyChangeListener(listener);
		}

		public void removePropertyChangeListener​(PropertyChangeListener listener)
		{
			this.changeSupport.removePropertyChangeListener(listener);
		}
	}
}