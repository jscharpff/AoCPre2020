package aoc2017.day18;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import aoc2017.day18.DuetAssembly.OutputConsumer;
import aocutil.assembly.RegisterAssemblyMachine.MachineState;

/**
 * Orchestrates multiple assembly machines running concurrently, sending and
 * receiving signals amongst them
 * 
 * @author Joris
 */
public class Orchestrator {
	/** The programs that are managed by this director */
	private final List<DuetAssembly> programs;
	
	/** The source code they are running */
	private final List<String> sourcecode;

	/** Number of output signals sent */
	private final Map<DuetAssembly, List<Long>> outputs;
	
	/**
	 * Creates a new orchestrator for duet programs
	 * 
	 * @param source The source code
	 * @param number The number of programs to run the code
	 */
	public Orchestrator( final List<String> source, final int number ) {
		programs = new ArrayList<>( number );
		IntStream.range( 0, number ).forEach( i -> {
			final DuetAssembly da = new DuetAssembly( "" + i );
			da.write( "p", i );
			programs.add( da );
		});
		this.sourcecode = new ArrayList<>( source );
		outputs = new HashMap<>( );
	}
	
	/**
	 * Starts the execution of the programs
	 * 
	 * @param program The program listing
	 */
	public void run( ) {
		outputs.clear( );
		
		// load programs and set up communication pipelines between programs
		for( int i = 0; i < programs.size( ); i++ ) {
			final DuetAssembly da = programs.get( i );
			outputs.put( da, new ArrayList<>( ) );			
			da.load( sourcecode );
			da.setOutputConsumer( new OutputConsumer( ) {
				@Override
				public void consume( long value ) {
					broadcast( da, value );					
				}
			} );
		}
		
		// alternately run programs until either both have terminated or in deadlock
		while( true ) {
			// set output count to output signal list sizes
			final int outcount = outputs.values( ).stream( ).mapToInt( Collection::size ).sum( );
			
			// continue execution of all programs until no more change in output is
			// observed, or they have terminated themselves
			for( final DuetAssembly da : programs ) {
				if( da.inState( MachineState.Yielded ) ) da.resume( );
			}

			// did the number of outputs change?
			final int newoutcount = outputs.values( ).stream( ).mapToInt( Collection::size ).sum( );	
			if( outcount == newoutcount ) break;
		};
	}
	
	/**
	 * Sends an input from the source to all other programs
	 * 
	 * @param source The source machine
	 * @param value The value to send as input to the others
	 * @return The number of receivers
	 */
	private int broadcast( final DuetAssembly source, final long value ) {
		// keep track of all observed outputs per source
		outputs.get( source ).add( value );
		
		// if there is only one machine broadcast the output to itself as input
		if( programs.size( ) == 1 ) {
			programs.get( 0 ).input( value );
			return 1;
		}

		// broadcast the output as input to all other machines
		int count = 0;
		for( final DuetAssembly da : programs ) {
			if( da.equals( source ) ) continue;
			
			da.input( value );
			count++;
		}		
		return count;
	}
	
	/** 
	 * Returns the observed output of the machine at the specified index  
	 * 
	 * @param index The index of the machine
	 * @return The observed output signals of the machine
	 */
	public List<Long> getOutput( final int index ) {
		return outputs.get( programs.get( index ) );
	}
}
