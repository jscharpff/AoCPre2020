package aoc2019.day22;

import java.math.BigInteger;

/**
 * Represents a simple linear function using big numbers
 * 
 * @author Joris
 */
public class BigLinearFunc {
	/** The first degree coefficient */
	protected final BigInteger a;
	
	/** The offset */
	protected final BigInteger b;
	
	/**
	 * Creates a new linear function y = ax + b
	 * 
	 * @param a
	 * @param b
	 */
	public BigLinearFunc( final long a, final long b ) {
		this( BigInteger.valueOf( a ), BigInteger.valueOf( b ) );
	}
	
	/**
	 * Creates a new linear function y = ax + b
	 * 
	 * @param a
	 * @param b
	 */
	public BigLinearFunc( final BigInteger a, final BigInteger b ) {
		this.a = a;
		this.b = b;
	}
	/**
	 * Applies the function to the given value for x
	 * 
	 * @param x
	 * @return The value that results from evaluating the function
	 */
	public BigInteger apply( final BigInteger x ) {
		return x.multiply( a ).add( b );
	}
	
	/**
	 * Builds the aggregate function h(x) = g(f(x)) such that this function is
	 * f(x) and the other function g(x) 
	 * 
	 * @param gx The function g(x) to combine it with
	 * @return The aggregate of both functions
	 */
	public BigLinearFunc aggregate( final BigLinearFunc gx ) {
		return new BigLinearFunc( a.multiply( gx.a ), gx.a.multiply( b ).add( gx.b ) );
	}
	
	/**
	 * Returns the modulo of the function
	 * 
	 * @param m The modulo 
	 * @return The modulo function
	 */
	public BigLinearFunc mod( final BigInteger m ) {
		return new BigLinearFunc( a.mod( m ), b.mod( m ) );
	}
	
	
	/** @return The function in the form y = ax + b */
	@Override
	public String toString( ) {
		return "y = " + a + "x " + (b.signum( ) >= 0 ? "+ " + b : "- " + b.negate( ) );
	}

}
