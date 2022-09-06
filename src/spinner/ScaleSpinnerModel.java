package spinner;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.SpinnerNumberModel;

public class ScaleSpinnerModel extends SpinnerNumberModel
{
	private int scale;

	public ScaleSpinnerModel(Number value, BigDecimal minimum, BigDecimal maximum, int scale)
	{
		this(ScaleSpinnerModel.getPreciseNumber(value, scale), minimum, maximum, scale);
	}

	public ScaleSpinnerModel(BigDecimal value, BigDecimal minimum, BigDecimal maximum, int scale)
	{
		super(value, minimum, maximum, ScaleSpinnerModel.getStepSize(scale));
		this.scale = scale;
	}

	@Override
	public void setValue(Object value)
	{
        if ((value == null) || !(value instanceof Number))
		{
            throw new IllegalArgumentException("illegal value");
        }
		BigDecimal newValue = this.getPreciseNumber((Number) value);
		super.setValue(newValue);
	}

	@Override
	public Object getNextValue()
	{
		return this.getBigDecimalValue().add(this.getBigDecimalStepSize());
	}
	
	@Override
	public Object getPreviousValue()
	{
		return this.getBigDecimalValue().subtract(this.getBigDecimalStepSize());
	}

	public void setScale(int scale)
	{
		this.scale = scale;
		this.setStepSize(ScaleSpinnerModel.getStepSize(scale));
		this.setValue(this.getValue());
	}

	public BigDecimal getBigDecimalValue()
	{
		if (this.getValue() == null)
		{
			return null;
		}
		return (BigDecimal) this.getValue();
	}

	public BigDecimal getBigDecimalStepSize()
	{
		Number stepSize = this.getStepSize();
		if (stepSize instanceof BigDecimal)
		{
			return (BigDecimal) stepSize;
		}
		else
		{
			return this.getPreciseNumber(stepSize);
		}
	}

	private static BigDecimal getPreciseNumber(Number number, int scale)
	{
		BigDecimal unscaled = BigDecimal.valueOf(number.doubleValue());
		return unscaled.setScale(scale, RoundingMode.HALF_UP);
	}

	private static BigDecimal getStepSize(int scale)
	{
		return ScaleSpinnerModel.getPreciseNumber(Math.pow(0.1d, scale), scale);
	}

	private BigDecimal getPreciseNumber(Number number)
	{
		return ScaleSpinnerModel.getPreciseNumber(number, this.scale);
	}
}
