package main.result;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import data.Range;
import data.functions.MapDiscreteData;
import hydraulics.ManningsFunction;
import hydraulics.ManningsModel;
import utility.Wrapper;

public class ResultScreenController
{
	private static final int DEFAULT_DISPLAYED_SCALE = 3;
	private static final String INITIAL_N = "0.025";
	private static final String INITIAL_S = "1";
	private static final String INITIAL_Q = "1";

	protected static enum ModelError
	{
		DISCHARGE_UNDERFLOW, CONSTANTS_NOT_SET, NOT_ENOUGH_DATA, INVALID_DISCHARGE_RANGE, NONE
	}

	protected class Result
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
	private Wrapper<BigDecimal> level;
	private Wrapper<BigDecimal> a;
	private Wrapper<BigDecimal> v;
	private ModelError error = ModelError.NONE;

	public ResultScreenController(MapDiscreteData<BigDecimal, BigDecimal> data)
	{
		this.model = new ManningsModel(data);
		this.function = new ManningsFunction(this.model);
		this.outputPrecision = new Wrapper<>(ResultScreenController.DEFAULT_DISPLAYED_SCALE);
		this.n = new Wrapper<>(new BigDecimal(ResultScreenController.INITIAL_N));
		this.s = new Wrapper<>(new BigDecimal(ResultScreenController.INITIAL_S));
		this.q = new Wrapper<>(new BigDecimal(ResultScreenController.INITIAL_Q));
		this.level = new Wrapper<>(null);
		this.a = new Wrapper<>(null);
		this.v = new Wrapper<>(null);

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

	public BigDecimal getLevel()
	{
		return this.level.value;
	}

	public BigDecimal getA()
	{
		return this.a.value;
	}

	public BigDecimal getV()
	{
		return this.v.value;
	}

	public ModelError getError()
	{
		return this.error;
	}

	public void updateModelConstants()
	{
		this.model.setN(this.n.value);
		this.model.setS(this.s.value);
	}

	public SwingWorker<?, ?> createWaterLevelWorker(int scale)
	{
		class CalcWorker extends SwingWorker<Double, Object>
		{
			private ManningsModel model;
			private double discharge;
			private int scale;
	
			public CalcWorker(ManningsModel model, double discharge, int scale)
			{
				super();
				this.model = new ManningsModel(model);
				this.discharge = discharge;
				this.scale = scale;
			}

			@Override
			protected Double doInBackground() throws Exception
			{
				if (this.model.areConstantsSet() && !this.model.dischargeUnderflow(this.discharge) && this.model.canUseData())
				{
					return this.model.calcWaterLevel(this.discharge, this.scale, () -> !this.isCancelled());
				}
				else
				{
					return null;
				}
			}

			@Override
			protected void done()
			{
				if (!this.isCancelled())
				{
					Double level = null;
					try
					{
						level = this.get();
					}
					catch (InterruptedException | ExecutionException e)
					{
						e.printStackTrace();
					}
					ResultScreenController.this.updateWaterLevel(this.discharge, level, this.scale);

					if (!this.model.areConstantsSet())
					{
						ResultScreenController.this.waterLevelError(ModelError.CONSTANTS_NOT_SET);
					}
					else if (this.model.dischargeUnderflow(this.discharge))
					{
						ResultScreenController.this.waterLevelError(ModelError.DISCHARGE_UNDERFLOW);
					}
					else if (!this.model.canUseData())
					{
						ResultScreenController.this.waterLevelError(ModelError.NOT_ENOUGH_DATA);
					}
					else
					{
						ResultScreenController.this.waterLevelError(ModelError.NONE);
					}
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
						results[i] = this.createErrorResult(ModelError.INVALID_DISCHARGE_RANGE);
					}
					return results;
				}
				for (int i = 0; i < this.numRows; i++)
				{
					results[i] = this.calcResult(this.dischargeRange.getNumber(this.specialDivide(i, (this.numRows - 1))), this.scale);
					if (results[i] == null)
					{
						return null;
					}
				}
				return results;
			}

			private Result calcResult(double discharge, int scale)
			{
				if (!this.model.areConstantsSet())
				{
					return this.createErrorResult(ModelError.CONSTANTS_NOT_SET);
				}
				else if (this.model.dischargeUnderflow(discharge))
				{
					return this.createErrorResult(ModelError.DISCHARGE_UNDERFLOW);
				}
				else if (!this.model.canUseData())
				{
					return this.createErrorResult(ModelError.NOT_ENOUGH_DATA);
				}

				Double level = this.model.calcWaterLevel(discharge, this.scale, () -> !this.isCancelled());
				if (level == null)
				{
					return null;
				}
				double velocity = this.model.calcVelocity(discharge, level);

				return new Result
				(
					ModelError.NONE,
					new BigDecimal(discharge).setScale(scale, RoundingMode.HALF_UP),
					new BigDecimal(level).setScale(scale, RoundingMode.HALF_UP),
					new BigDecimal(velocity).setScale(scale, RoundingMode.HALF_UP)
				);
			}

			private double specialDivide(double dividend, double divisor)
			{
				return dividend == 0 && divisor == 0 ? 0 : dividend / divisor;
			}

			private Result createErrorResult(ModelError error)
			{
				return new Result(error, null, null, null);
			}
		}
		if (numRows < 0)
		{
			throw new IllegalArgumentException("Cannot have negative number of rows");
		}
		return new TableCalcWorker(this.model, minDischarge, maxDischarge, numRows, scale);
	}

	private void updateWaterLevel(double discharge, Double level, int scale)
	{
		if (level == null || (discharge != 0 && this.model.calcArea(level) == 0))
		{
			this.level.value = null;
			this.a.value = null;
			this.v.value = null;
			return;
		}
		this.level.value = new BigDecimal(level).setScale(scale, RoundingMode.HALF_UP);
		this.a.value = new BigDecimal(this.model.calcArea(level)).setScale(scale, RoundingMode.HALF_UP);
		this.v.value = new BigDecimal(this.model.calcVelocity(discharge, level)).setScale(scale, RoundingMode.HALF_UP);
	}

	private void waterLevelError(ModelError error)
	{
		this.error = error;
	}
}
