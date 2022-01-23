package main.result;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import data.MapDiscreteData;
import hydraulics.ManningsFunction;
import hydraulics.ManningsModel;
import ui.Wrapper;

public class ResultScreenController
{
	private static final String INITIAL_N = "0.025";
	private static final String INITIAL_S = "1";
	private static final String INITIAL_Q = "1";

	protected static enum ModelError
	{
		DISCHARGE_UNDERFLOW, CONSTANTS_NOT_SET, NOT_ENOUGH_DATA, NONE
	}

	private ManningsModel model;
	private ManningsFunction function;
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

	public SwingWorker<?, ?> createCalcDialogWorker(int scale)
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
