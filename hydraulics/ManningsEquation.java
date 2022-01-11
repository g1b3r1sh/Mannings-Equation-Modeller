package hydraulics;

/**
 * Represents an instance of the Manning's equation
 * The Manning's equation is an equation used to calculate the discharge of a cross-section of a river
 * When combined with the discharge formula Q = VA, it can also be used to calculate the velocity of a cross-section
 * Variables must be initialized before they are used, otherwise, methods will throw a NullPointerException
**/

@SuppressWarnings("unqualified-field-access")
public class ManningsEquation
{
	// Since the class uses doubles, results of equation are unlikely to be exact, meaning
	// there must be a threshold in which the results are "good enough"
	public static final double ACCEPTABLE_THRESHOLD = 0.00001;

	// Q = VA = (1/n)A(R^2/3)(s^1/2)
	// However, given that R = A / P, it can be substituted into the equation, resulting in
	// Q = VA = (1/n)A((A/P)^2/3)(s^1/2)
	public Double q = null; // Discharge, Q (m^3/s)

	public Double v = null; // Flow Velocity, V (m/s)
	public Double a = null; // Cross-section Area, A (m^2)

	public Double p = null; // Wetted Perimeter, P (m)
	public Double n = null; // Manning’s Roughness Coefficient, n
	public Double s = null; // Channel Bed Slope, S

	public boolean manningFormulaConsistent()
	{
		return Math.abs(q - this.calcDischarge()) < ACCEPTABLE_THRESHOLD;
	}
	
	public double r() // Hydraulic Radius, R (m)
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
