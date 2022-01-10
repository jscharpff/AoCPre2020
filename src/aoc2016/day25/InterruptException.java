package aoc2016.day25;

/**
 * Exception to force quit
 */
@SuppressWarnings( "serial" )
public class InterruptException extends Exception {
	/** The exit code */
	private final int statuscode;
	
	/**
	 * Creates a new interrupt exception
	 * 
	 * @param code The status code
	 */
	public InterruptException( final int code ) {
		this( code, "Interrupted" );
	}
	
	/**
	 * Creates a new interrupt exception
	 * 
	 * @param code The status code
	 * @param message The interrupt message
	 */
	public InterruptException( final int code, final String message ) {
		super( message + " (code " + code + ")" );
		this.statuscode = code;
	}
	
	/** @return The status code */
	public int getCode( ) { return statuscode; }
}