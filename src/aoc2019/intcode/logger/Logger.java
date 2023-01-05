package aoc2019.intcode.logger;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import aoc2019.intcode.IntCode;

/**
 * Logger for the intcode machine
 * 
 * @author Joris
 */
public class Logger {
	/** Reference to its program */
	protected final IntCode program;
	
	/** The types of logging message */
	protected Set<Logtype> logtypes;
	
	/** The enum of available log types */
	public enum Logtype {
		Info, 
		Debug,
		Warning,
		Error;
	}
	
	/** The log stream to use for normal logging */
	protected PrintStream stdout;
	
	/** The log stream to use for error logging */
	protected PrintStream stderr;

	/**
	 * Creates a new logger that logs the specified types
	 * 
	 * @param program The reference to its IntCode program
	 * @param logtypes The log types
	 */
	public Logger( final IntCode program, Logtype... logtypes ) {		
		this.stdout = System.out;
		this.stderr = System.err;
		
		this.program = program;
		this.setLogtypes( logtypes );
	}
	
	/**
	 * Creates a new logger that logs no messages
	 * 
	 * @param program The program to log for
	 */
	public Logger( final IntCode program ) {
		this( program, new Logtype[] { } );
	}
	
	/**
	 * Sets the log types that are being logged by this logger
	 * 
	 * @param logtypes The log types
	 */
	public void setLogtypes( final Logtype... logtypes ) {
		this.logtypes = new HashSet<>( );
		for( Logtype l : logtypes ) this.logtypes.add( l );
	}
	
	/**
	 * Logs the specified message with the log type (if configured)
	 * 
	 * @param type The required log type
	 * @param message The message to log
	 */
	public void log( final Logtype type, final String message ) {
		// should we log this message at all?
		if( !logtypes.contains( type ) ) return;
		
		// treat warnings and errors differently
		print( (type == Logtype.Warning || type == Logtype.Error) ? stderr : stdout, "[" + program.getName( ) + " - " + type + "] " + message + "\n" );
	}
	
	/**
	 * Prints the actual log message using the specified stream, silently ignores
	 * the message if the stream is not set
	 * 
	 * @param stream The stream to use
	 * @param message The actual message to log
	 */
	protected void print( final PrintStream stream, final String message ) {
		if( stream == null ) return;
		stream.print( message );
	}
	
	/**
	 * Shorthand to log an info message
	 * 
	 * @param message The informational message
	 */
	public void info( final String message ) { log( Logtype.Info, message ); }
	
	/**
	 * Shorthand to log a debug message
	 * 
	 * @param message The debug message
	 */
	public void debug( final String message ) { log( Logtype.Debug, message ); }
}
