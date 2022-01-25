package spinner;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.SpinnerNumberModel;

public class PrecisionSpinnerModel extends SpinnerNumberModel
{
	private int precision;

	public PrecisionSpinnerModel(Number value, BigDecimal minimum, BigDecimal maximum, int precision)
	{
		this(PrecisionSpinnerModel.getPreciseNumber(value, precision), minimum, maximum, precision);
	}

	public PrecisionSpinnerModel(BigDecimal value, BigDecimal minimum, BigDecimal maximum, int precision)
	{
		super(value, minimum, maximum, PrecisionSpinnerModel.getStepSize(precision));
		this.precision = precision;
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

	public void setPrecision(int precision)
	{
		this.precision = precision;
		this.setStepSize(PrecisionSpinnerModel.getStepSize(precision));
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

	private static BigDecimal getPreciseNumber(Number number, int precision)
	{
		BigDecimal unscaled = BigDecimal.valueOf(number.doubleValue());
		return unscaled.setScale(precision, RoundingMode.HALF_UP);
	}

	private static BigDecimal getStepSize(int precision)
	{
		return PrecisionSpinnerModel.getPreciseNumber(Math.pow(0.1d, precision), precision);
	}

	private BigDecimal getPreciseNumber(Number number)
	{
		return PrecisionSpinnerModel.getPreciseNumber(number, this.precision);
	}
}
