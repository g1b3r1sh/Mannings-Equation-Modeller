package main;

import java.math.BigDecimal;

import data.DataScale;
import data.functions.MapDiscreteData;
import hydraulics.WaterLevelCalculator;

public class CrossSectionModel
{
	private MapDiscreteData<BigDecimal, BigDecimal> data = CrossSectionModel.defaultData();
	private DataScale scale = CrossSectionModel.defaultScale();
	private WaterLevelCalculator<BigDecimal, BigDecimal> waterCalculator = new WaterLevelCalculator<BigDecimal, BigDecimal>(this.data, defaultWaterLevel());

	public MapDiscreteData<BigDecimal, BigDecimal> getData()
	{
		return this.data;
	}

	public DataScale getScale()
	{
		return this.scale;
	}

	public WaterLevelCalculator<BigDecimal, BigDecimal> getCalculator()
	{
		return this.waterCalculator;
	}

	public void loadSampleData()
	{
		this.data.load(defaultData());
		this.scale.load(defaultScale());
		this.waterCalculator.setWaterLevel(defaultWaterLevel());
	}
	
	public static MapDiscreteData<BigDecimal, BigDecimal> defaultData()
	{

		MapDiscreteData<BigDecimal, BigDecimal> data = new MapDiscreteData<>();
		
		data.set(new BigDecimal("0.254"), new BigDecimal("4.12"));
		data.set(new BigDecimal("1.000"), new BigDecimal("1.00"));
		data.set(new BigDecimal("2.000"), new BigDecimal("3.00"));
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
