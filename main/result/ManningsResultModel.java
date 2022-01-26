package main.result;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.BooleanSupplier;

import javax.swing.SwingWorker;

import data.Range;
import data.functions.MapDiscreteData;
import hydraulics.ManningsFunction;
import hydraulics.ManningsModel;
import utility.Wrapper;

public class ManningsResultModel
{
	private static final int DEFAULT_DISPLAYED_SCALE = 3;
	private static final String INITIAL_N = "0.025";
	private static final String INITIAL_S = "1";
	private static final String INITIAL_Q = "1";

	protected static enum ModelError
	{
		DISCHARGE_UNDERFLOW, CONSTANTS_NOT_SET, NOT_ENOUGH_DATA, INVALID_DISCHARGE_RANGE, NONE
	}

	protected static class Result
	{
		private ModelError error;
		private BigDecimal q;
		private BigDecimal level;
		private BigDecimal v;

		public Result(ModelError error, BigDecimal q, BigDecimal level, BigDecimal v)
		{
			this.error = error;
			this.q = q;
			this.level = level;
			this.v = v;
		}

		public ModelError getError()
		{
			return this.error;
		}

		public BigDecimal getQ()
		{
			return this.q;
		}

		public BigDecimal getLevel()
		{
			return this.level;
		}

		public BigDecimal getV()
		{
			return this.v;
		}
	}

	private ManningsModel model;
	private ManningsFunction function;
	private Wrapper<Integer> outputPrecision;
	private Wrapper<BigDecimal> n;
	private Wrapper<BigDecimal> s;
	private Wrapper<BigDecimal> q;

	public ManningsResultModel(MapDiscreteData<BigDecimal, BigDecimal> data)
	{
		this.model = new ManningsModel(data);
		this.function = new ManningsFunction(this.model);
		this.outputPrecision = new Wrapper<>(ManningsResultModel.DEFAULT_DISPLAYED_SCALE);
		this.n = new Wrapper<>(new BigDecimal(ManningsResultModel.INITIAL_N));
		this.s = new Wrapper<>(new BigDecimal(ManningsResultModel.INITIAL_S));
		this.q = new Wrapper<>(new BigDecimal(ManningsResultModel.INITIAL_Q));
		this.updateModelConstants();
	}

	public ManningsFunction getFunction()
	{
		return this.function;
	}

	protected int getOutputPrecision()
	{
		return this.outputPrecision.value;
	}

	protected void setOutputPrecision(int precision)
	{
		this.outputPrecision.value = precision;
	}

	public BigDecimal getN()
	{
		return this.n.value;
	}

	public void setN(BigDecimal n)
	{
		this.n.value = n;
	}

	public BigDecimal getS()
	{
		return this.s.value;
	}

	public void setS(BigDecimal s)
	{
		this.s.value = s;
	}

	public BigDecimal getQ()
	{
		return this.q.value;
	}

	public void setQ(BigDecimal q)
	{
		this.q.value = q;
	}

	public void updateModelConstants()
	{
		this.model.setN(this.n.value);
		this.model.setS(this.s.value);
	}

	private static Result calcResult(ManningsModel model, double discharge, int scale, BooleanSupplier cancelFunction)
	{
		if (!model.areConstantsSet())
		{
			return ManningsResultModel.createErrorResult(ModelError.CONSTANTS_NOT_SET);
		}
		else if (model.dischargeUnderflow(discharge))
		{
			return ManningsResultModel.createErrorResult(ModelError.DISCHARGE_UNDERFLOW);
		}
		else if (!model.canUseData())
		{
			return ManningsResultModel.createErrorResult(ModelError.NOT_ENOUGH_DATA);
		}

		Double level = model.calcWaterLevel(discharge, scale, () -> !cancelFunction.getAsBoolean());
		if (level == null)
		{
			return null;
		}
		double velocity = model.calcVelocity(discharge, level);

		return new Result
		(
			ModelError.NONE,
			new BigDecimal(discharge).setScale(scale, RoundingMode.HALF_UP),
			new BigDecimal(level).setScale(scale, RoundingMode.HALF_UP),
			new BigDecimal(velocity).setScale(scale, RoundingMode.HALF_UP)
		);
	}

	private static Result createErrorResult(ModelError error)
	{
		return new Result(error, null, null, null);
	}

	public SwingWorker<Result, ?> createWaterLevelWorker(int scale)
	{
		class CalcWorker extends SwingWorker<Result, Object>
		{
			private ManningsModel model;
			private double discharge;
			private int scale;
	
			public CalcWorker(ManningsModel model, double discharge, int scale)
			{
				super();
				// Copy model to avoid threading conflicts
				this.model = new ManningsModel(model);
				this.discharge = discharge;
				this.scale = scale;
			}

			@Override
			protected Result doInBackground() throws Exception
			{
				if (this.model.areConstantsSet() && !this.model.dischargeUnderflow(this.discharge) && this.model.canUseData())
				{
					return ManningsResultModel.calcResult(this.model, this.discharge, this.scale, this::isCancelled);
				}
				else
				{
					return null;
				}
			}
		}
		
		return new CalcWorker(this.model, this.q.value.doubleValue(), scale);
	}

	public SwingWorker<Result[], ?> createResultsWorker(double minDischarge, double maxDischarge, int numRows, int scale)
	{
		class TableCalcWorker extends SwingWorker<Result[], Object>
		{
			private ManningsModel model;
			private Range.Double dischargeRange;
			private int numRows;
			private int scale;

			public TableCalcWorker(ManningsModel model, double minDischarge, double maxDischarge, int numRows, int scale)
			{
				super();
				// Copy model to avoid threading conflicts
				this.model = new ManningsModel(model);
				this.dischargeRange = minDischarge > maxDischarge ? null : new Range.Double(minDischarge, maxDischarge);
				this.numRows = numRows;
				this.scale = scale;
			}

			@Override
			protected Result[] doInBackground() throws Exception
			{
				Result[] results = new Result[this.numRows];
				if (this.dischargeRange == null)
				{
					for (int i = 0; i < results.length; i++)
					{
						results[i] = ManningsResultModel.createErrorResult(ModelError.INVALID_DISCHARGE_RANGE);
					}
					return results;
				}
				for (int i = 0; i < this.numRows; i++)
				{
					results[i] = ManningsResultModel.calcResult(this.model, this.dischargeRange.getNumber(this.specialDivide(i, (this.numRows - 1))), this.scale, this::isCancelled);
					if (results[i] == null)
					{
						return null;
					}
				}
				return results;
			}

			private double specialDivide(double dividend, double divisor)
			{
				return dividend == 0 && divisor == 0 ? 0 : dividend / divisor;
			}
		}
		if (numRows < 0)
		{
			throw new IllegalArgumentException("Cannot have negative number of rows");
		}
		return new TableCalcWorker(this.model, minDischarge, maxDischarge, numRows, scale);
	}
}
