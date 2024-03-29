package main;

import java.math.BigDecimal;

import data.DataScale;
import data.functions.DiscreteData;
import data.functions.MapDiscreteData;
import data.functions.MutableDiscreteData;
import hydraulics.CrossSectionCalculator;

public class CrossSectionDataset
{
	private MutableDiscreteData<BigDecimal, BigDecimal> data = CrossSectionDataset.defaultData();
	private DataScale scale = CrossSectionDataset.defaultScale();
	private CrossSectionCalculator<BigDecimal, BigDecimal> crossSectionCalculator = new CrossSectionCalculator<BigDecimal, BigDecimal>(this.data, defaultWaterLevel());

	public CrossSectionDataset() { }

	public CrossSectionDataset(DiscreteData<BigDecimal, BigDecimal> data, DataScale scale)
	{
		this.data.load(data);
		this.scale.load(scale);
	}

	public DiscreteData<BigDecimal, BigDecimal> getData()
	{
		return this.data;
	}

	public MutableDiscreteData<BigDecimal, BigDecimal> getMutableData()
	{
		return this.data;
	}

	public DataScale getScale()
	{
		return this.scale;
	}

	public CrossSectionCalculator<BigDecimal, BigDecimal> getCalculator()
	{
		return this.crossSectionCalculator;
	}

	public void load(DiscreteData<BigDecimal, BigDecimal> data, DataScale scale)
	{
		this.data.load(data);
		this.scale.load(scale);
	}

	public void loadSampleData()
	{
		this.data.load(defaultData());
		this.scale.load(defaultScale());
		this.crossSectionCalculator.setWaterLevel(defaultWaterLevel());
	}
	
	public static MutableDiscreteData<BigDecimal, BigDecimal> defaultData()
	{
		MapDiscreteData<BigDecimal, BigDecimal> data = new MapDiscreteData<>();
		
		data.set(new BigDecimal("0.254"), new BigDecimal("4.12"));
		data.set(new BigDecimal("1.000"), new BigDecimal("1.50"));
		data.set(new BigDecimal("2.000"), new BigDecimal("0.90"));
		data.set(new BigDecimal("4.234"), new BigDecimal("0.26"));
		data.set(new BigDecimal("5.723"), new BigDecimal("3.26"));
		data.set(new BigDecimal("6.320"), new BigDecimal("3.55"));
		data.set(new BigDecimal("8.242"), new BigDecimal("4.16"));
		data.set(new BigDecimal("9.121"), new BigDecimal("4.80"));
		
		return data;
	}

	public static DataScale defaultScale()
	{
		return new DataScale(3, 2);
	}

	public static BigDecimal defaultWaterLevel()
	{
		return new BigDecimal("2.00");
	}
}
