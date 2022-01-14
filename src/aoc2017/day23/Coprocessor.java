package aoc2017.day23;

import aocutil.assembly.RegisterAssemblyMachine;
import aocutil.assembly.YieldException;

/**
 * Assembly driven co-processor
 * @author Joris
 *
 */
public class Coprocessor extends RegisterAssemblyMachine {
	/** The number of times the multiply instruction is invoked */
	private long mulcount;
	
	/**
	 * Creates a new Coprosessor
	 */
	public Coprocessor( ) {
		super( "Coprossesor" );
		mulcount = 0;
	}

	/**
	 * Executes a single instruction
	 */
	@Override
	protected void execute( final String instruction ) throws YieldException {
		System.out.println( read( "b" ) );
		
		final String[] i = instruction.split( " " );
		switch( i[0] ) {
			
			case "set": write( i[1], read( i[2] ) ); break;
			case "sub": write( i[1], read( i[1] ) - read( i[2] ) ); break;
			case "mul": write( i[1], read( i[1] ) * read( i[2] ) ); mulcount++; break;
			case "div": write( i[1], read( i[1] ) / read( i[2] ) ); break;
			case "mod": write( i[1], read( i[1] ) % read( i[2] ) ); break;
			
			
			case "jnz": jumpIf( read( i[1] ) != 0, read( i[2]) ); break;
			case "jgz": jumpIf( read( i[1] ) > 0, read( i[2]) ); break;
			
			default:
				throw new RuntimeException( "Unsupported instruction: " + instruction );
		}
	}
	
	/** @return The number of times the multiply instruction is invoked */
	public long getMultiplyCount( ) { return mulcount; }

}
