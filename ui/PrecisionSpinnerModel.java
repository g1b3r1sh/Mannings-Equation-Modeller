package ui;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.SpinnerNumberModel;

public class PrecisionSpinnerModel extends SpinnerNumberModel
{
	private int precision;

	public PrecisionSpinnerModel(Number value, BigDecimal minimum, BigDecimal maximum, int precision)
	{
		this(PrecisionSpinnerModel.generatePreciseNumber(value, precision), minimum, maximum, precision);
	}

	public PrecisionSpinnerModel(BigDecimal value, BigDecimal minimum, BigDecimal maximum, int precision)
	{
		super(value, minimum, maximum, PrecisionSpinnerModel.generateStepSize(precision));
		this.precision = precision;
	}

	@Override
	public void setValue(Object value)
	{
        if ((value == null) || !(value instanceof Number))
		{
            throw new IllegalArgumentException("illegal value");
        }
		BigDecimal newValue = this.createPreciseNumber((Number) value);
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
		this.setStepSize(PrecisionSpinnerModel.generateStepSize(precision));
		this.setValue(this.getValue());
	}

	public BigDecimal getBigDecimalValue()
	{
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
			return this.createPreciseNumber(stepSize);
		}
	}

	private static BigDecimal generatePreciseNumber(Number number, int precision)
	{
		BigDecimal unscaled = BigDecimal.valueOf(number.doubleValue());
		return unscaled.setScale(precision, RoundingMode.HALF_UP);
	}

	private static BigDecimal generateStepSize(int precision)
	{
		return PrecisionSpinnerModel.generatePreciseNumber(Math.pow(0.1d, precision), precision);
	}

	private BigDecimal createPreciseNumber(Number number)
	{
		return PrecisionSpinnerModel.generatePreciseNumber(number, this.precision);
	}
}
