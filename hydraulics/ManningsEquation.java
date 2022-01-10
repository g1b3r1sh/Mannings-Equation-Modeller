package hydraulics;

public class ManningsEquation
{
	public static final double ACCEPTABLE_THRESHOLD_MANNING = 0.00001;

	// Q = VA = (1/n)A(R^2/3)(s^1/2)
	// However, given that R = A / P, it can be substituted into the equation, giving
	// Q = VA = (1/n)A((A/P)^2/3)(s^1/2)
	public Double q = null; // Discharge, Q

	public Double v = null; // Flow Velocity, V
	public Double a = null; // Cross-section Area, A

	public Double p = null; // Wetted Perimeter, P
	public Double n = null; // Manning’s Roughness Coefficient, n
	public Double s = null; // Channel Bed Slope, S

	public boolean manningFormulaConsistent()
	{
		return (q - this.calcDischarge()) < ACCEPTABLE_THRESHOLD_MANNING;
	}
	
	public double r()
	{
		return a / p;
	}

	public double calcDischarge()
	{
		return (1 / n) * a * Math.pow(r(), 2d / 3) * Math.sqrt(s);
	}

	public double calcVelocity()
	{
		return q / a;
	}
}
