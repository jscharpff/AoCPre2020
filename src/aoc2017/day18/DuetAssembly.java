package aoc2017.day18;

import java.util.ArrayList;
import java.util.List;

import aocutil.assembly.RegisterAssemblyMachine;
import aocutil.assembly.YieldException;

/**
 * A assembly machine that is capable of sending and receiving signals
 * 
 * @author Joris
 */
public class DuetAssembly extends RegisterAssemblyMachine {
	/** The input queue */
	private final List<Long> inputQ;
	
	/** The output consumer */
	private OutputConsumer output;
	
	/**
	 * Creates a new DuetAssembly machine
	 * 
	 * @param label The label of the machine
	 */
	public DuetAssembly( final String label ) {
		super( label );
		
		this.inputQ = new ArrayList<>( );
		
		// by default print output to console
		output = new OutputConsumer( ) {
			@Override	public void consume( long value ) { System.out.println( value ); } };
	}
	
	/** @return The label */
	public String getLabel( ) { return label; }
	
	/**
	 * Sets the output consumer for this machine
	 * 
	 * @param consumer
	 */
	public void setOutputConsumer( final OutputConsumer consumer ) {
		this.output = consumer;
	}
	
	/**
	 * Sends a single input value to this machine
	 * 
	 * @param value The input value
	 */
	public void input( final long value ) {
		inputQ.add( value );
	}
	
	/**
	 * Checks the state of the machine 
	 *
	 *@param checkstate The state to check against
	 * @return True if the current state is equal to the checkstate
	 */
	public boolean inState( final MachineState checkstate ) { return getState( ) == checkstate; } 
	
	/**
	 * Executes a single instruction
	 * 
	 * @param instruction The instruction to perform 
	 */
	@Override
	protected void execute( final String instruction ) throws YieldException {
		final String[] i = instruction.split( " " );
		
		switch( i[0] ) {
			// sound
			case "snd": send( read( i[1] ) ); break;
			case "rcv": write( i[1], receive( ) ); break;
			
			// arithmetic
			case "set": write( i[1], read( i[2] ) ); break;
			case "add": write( i[1], read( i[1] ) + read( i[2] ) ); break;
			case "mul": write( i[1], read( i[1] ) * read( i[2] ) ); break;
			case "mod": write( i[1], read( i[1] ) % read( i[2] ) ); break;
			
			// jumps
			case "jgz": jumpIf( read( i[1] ) > 0, read( i[2] ) ); break;

			default:
				throw new RuntimeException( "Unsupported instruction: " + instruction );
		}
	}
	
	/**
	 * Sends a value as output signal
	 * 
	 * @param value The value to send
	 */
	private void send( final long value ) {
		output.consume( value );
	}
	
	/**
	 * Receives the last emitted frequency if the argument is not zero
	 * 
	 * @param arg The argument
	 * @return True if a signal was received
	 * @throws YieldException if no input was available
	 */
	public long receive( ) throws YieldException {
		// try to read an input value, if no value is available yield the program
		// execution
		if( inputQ.size( ) == 0 ) {
			jump( 0 ); 
			throw new YieldException( 0, "Wating for input" );
		}
		
		// remove first entry from the list
		return inputQ.remove( 0 );
	}
	
	/**
	 * Interface for output consumers
	 */
	public interface OutputConsumer {
		/**
		 * Consumes a single value of output 
		 * 
		 * @param value
		 */
		public void consume( final long value );
	}
}
