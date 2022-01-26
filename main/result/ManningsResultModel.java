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
	protected static final int MIN_DISPLAY_SCALE = 0;
	protected static final int MAX_DISPLAY_SCALE = 9;
	private static final int DEFAULT_DISPLAY_SCALE = 3;

	private static final String INITIAL_N = "0.025";
	private static final String INITIAL_S = "1";
	private static final String INITIAL_Q = "1";

	protected static final int MIN_NUM_DISCHARGE_ROWS = 0;
	private static final BigDecimal DEFAULT_MIN_DISCHARGE = new BigDecimal(0);
	private static final BigDecimal DEFAULT_MAX_DISCHARGE = new BigDecimal(500);
	private static final int DEFAULT_NUM_DISCHARGE_ROWS = 5;

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

	private Wrapper<BigDecimal> n;
	private Wrapper<BigDecimal> s;
	private Wrapper<BigDecimal> q;

	private Wrapper<Integer> outputPrecision;
	private Wrapper<BigDecimal> dischargeLower;
	private Wrapper<BigDecimal> dischargeUpper;
	private Wrapper<Integer> numDischargeRows;

	public ManningsResultModel(MapDiscreteData<BigDecimal, BigDecimal> data)
	{
		this.model = new ManningsModel(data);
		this.function = new ManningsFunction(this.model);

		this.n = new Wrapper<>(new BigDecimal(ManningsResultModel.INITIAL_N));
		this.s = new Wrapper<>(new BigDecimal(ManningsResultModel.INITIAL_S));
		this.q = new Wrapper<>(new BigDecimal(ManningsResultModel.INITIAL_Q));

		this.outputPrecision = new Wrapper<>(ManningsResultModel.DEFAULT_DISPLAY_SCALE);
		this.dischargeLower = new Wrapper<>(ManningsResultModel.DEFAULT_MIN_DISCHARGE);
		this.dischargeUpper = new Wrapper<>(ManningsResultModel.DEFAULT_MAX_DISCHARGE);
		this.numDischargeRows = new Wrapper<>(ManningsResultModel.DEFAULT_NUM_DISCHARGE_ROWS);

		this.updateModelConstants();
	}

	public ManningsFunction getFunction()
	{
		return this.function;
	}

	protected Wrapper<Integer> getOutputPrecision()
	{
		return this.outputPrecision;
	}

	public Wrapper<BigDecimal> getN()
	{
		return this.n;
	}

	public Wrapper<BigDecimal> getS()
	{
		return this.s;
	}

	public Wrapper<BigDecimal> getQ()
	{
		return this.q;
	}

	public Wrapper<BigDecimal> getDischargeLower()
	{
		return this.dischargeLower;
	}

	public Wrapper<BigDecimal> getDischargeUpper()
	{
		return this.dischargeUpper;
	}

	public Wrapper<Integer> getNumDischargeRows()
	{
		return this.numDischargeRows;
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
