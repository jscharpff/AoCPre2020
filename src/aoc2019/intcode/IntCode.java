package aoc2019.intcode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import aoc2019.intcode.exceptions.ICEInputOutput;
import aoc2019.intcode.exceptions.ICEInvalidState;
import aoc2019.intcode.exceptions.ICEInvalidSyntax;
import aoc2019.intcode.exceptions.ICEMemoryInvalid;
import aoc2019.intcode.exceptions.ICERuntimeException;
import aoc2019.intcode.exceptions.ICEUnsupported;
import aoc2019.intcode.exceptions.ICEYield;
import aoc2019.intcode.instructions.Argument;
import aoc2019.intcode.instructions.Argument.AddressingMode;
import aoc2019.intcode.instructions.Instruction;
import aoc2019.intcode.instructions.InstructionSet;
import aoc2019.intcode.io.IOManager;
import aoc2019.intcode.io.VisualIOManager;
import aoc2019.intcode.logger.Logger;
import aoc2019.intcode.logger.Logger.Logtype;
import aocutil.string.StringUtil;

/**
 * IntCode program, runs instructions using integer codes for operands
 * 
 * @author Joris
 */
public class IntCode {
	/** This machine's name */
	protected final String name;
	
	/** The current program */
	protected List<Long> program;
	
	/** The instruction pointer */
	protected int IP;
	
	/** The current relative memory base */
	protected int MEMBASE;
	
	/** True iff the program is actively being run */
	protected IntCodeState state;
	
	/** Instruction that is being executed */
	protected Instruction instruction;
	
	/** The logger */
	protected final Logger logger;
	
	/** The program IO manager */
	protected IOManager io; 
	
	/** Possible states the program can be in */
	public enum IntCodeState {
		Initialising, Running, AwaitingInput, Ended, Halted;
		
		public boolean isRunning( ) { return this == Running; }
		public boolean isEnded( ) { return this == Ended || this == Halted; }
	}
	
	/** The visualisation modes of the machine */
	public enum VisualMode { None, IntCode, ASCII; }
	
	/** Run in threaded mode */
	protected boolean threaded;
	
	/**
	 * Creates a new IntCode machine for the specified program listing
	 * 
	 * @param name The program name
	 * @param listing The program instruction set
	 */
	public IntCode( final String name, final List<Long> listing ) {
		this.name = name;
		this.program = listing;
		
		this.logger = new Logger( this );
		
		// invalidate typical runtime informatio
		this.IP = -1;
		this.MEMBASE = -1;
		state = IntCodeState.Initialising;
		
		try {
			setVisualisationMode( VisualMode.None );
		} catch( ICEInvalidState e ) {
			/* never thrown */
		}
	}
	
	/**
	 * Sets the logtypes of the logger
	 * 
	 * @param logtypes The types of messages to log
	 */
	public void setLogtypes( final Logtype... logtypes ) {
		logger.setLogtypes( logtypes );
	}
	
	/** @return The program name */
	public String getName( ) { return name; }
	
	/** @return The instruction pointer */
	public long getIP( ) { return IP; }
	
	/** @return The current state of the program */
	public IntCodeState getState( ) { return state; }
	
	/** @return True iff the program is running */
	public boolean isRunning( ) { return state.isRunning( ); }
	
	/** @return True if the program is awaiting input */
	public boolean isWaiting( ) { return state == IntCodeState.AwaitingInput; }
	
	/** 
	 * @param index The position od the int code to get
	 * @return The intcode value at the specified position
	 * */
	public long getIntcodeAt( final int index ) { return program.get( index ); }
	
	/**
	 * Sets the visualisation to the specified mode 
	 * 
	 * @param mode The visualisation mode
	 * @throws ICEUnsupported if the mode is unsupported
	 * @throws ICEInvalidState if the state is not initialising
	 */
	public void setVisualisationMode( final VisualMode mode ) throws ICEInvalidState {
		ICEInvalidState.throwIfNotValid( this, IntCodeState.Initialising, "Can only set visualisation mode in the initialing state" );
		
		switch( mode ) {
			case None: this.io = new IOManager( this ); break;
			case IntCode: this.io = new VisualIOManager( this, getName( ) + " - IntCode output", false ); break;
			case ASCII: this.io = new VisualIOManager( this, getName( ) + " - ASCII output", true ); break;
			default:
				throw new RuntimeException( "Visualisation mode " + mode + " unsupported" );
		}
	}
	
	/**
	 * Runs the program in terminal mode that enables simple IO
	 * 
	 * @param inputfunc The function to process terminal commands
	 * @throws ICEInvalidState if the program is not initialising
	 */
	public void setTerminalMode( final Consumer<String> inputfunc ) throws ICEInvalidState {
		setVisualisationMode( VisualMode.ASCII );
		final VisualIOManager VIO = (VisualIOManager)io;
		VIO.getDisplay( ).addInputListener( inputfunc );
		VIO.getDisplay( ).setAppendMode( true );
	}
	
	/**
	 * Changes the intcode value at the specified index
	 * 
	 * @param index The index to change
	 * @param intcode The intcode to set
	 * @return The previous value at the index
	 */
	public long setIntcodeAt( final int index, final long intcode ) {
		final long prev = getIntcodeAt( index );
		program.set( index, intcode );
		return prev;
	}
	
	/**
	 * Sets starting input for the IntCode machine
	 * 
	 * @param input The input values
	 * @throws ICEInvalidState if the program is already running
	 */
	public void setInput( final long... input ) throws ICEInvalidState {
		ICEInvalidState.throwIfNotValid( this, IntCodeState.Initialising, "Cannot set input variables of a running program (use feed instead)" );
		
		io.feed( input );
	}
	
	/**
	 * Retrieves the resulting output after the program terminated
	 * 
	 * @return The (remaining) output buffer
	 * @throws ICEInvalidState if the program is still running
	 */
	public long[] getOutput( ) throws ICEInvalidState {
		ICEInvalidState.throwIfNotValid( this, IntCodeState.Ended, "Cannot get program output of a running program (use consume instead)" );
		
		return io.getOutputBuffer( );
	}

	/**
	 * Runs the program!
	 * 
	 * @return The state in which the program ends
	 * @throws ICERuntimeException on any error encountered during the program run
	 * @throws ICEInvalidState if the run was called from an invalid state
	 */
	public IntCodeState run( ) throws ICERuntimeException {
		ICEInvalidState.throwIfNotValid( this, IntCodeState.Initialising );
		
		// initialise IP and MEMBASE if just starting now
		this.IP = 0;
		this.MEMBASE = 0;
		this.threaded = false;
				
		return runprogram( );
	}
	
	/**
	 * Resumes program execution after processing an input/output value. May also 
	 * be used to start execution of a new program
	 * 
	 * @return The state in which the program ends
	 * @throws ICERuntimeException if the program encounters any error during execution 
	 */
	public IntCodeState resume( ) throws ICERuntimeException {
		ICEInvalidState.throwIfNotValid( this, "Can only resume from awaiting states", IntCodeState.Initialising, IntCodeState.AwaitingInput );

		// start program fresh?
		if( state == IntCodeState.Initialising ) return run();
		else return runprogram( );
	}
	
	/**
	 * Runs the actual program until execution ends or yields
	 * 
	 * @return The state the program ended in
	 * @throws ICERuntimeException for any runtime exception during execution of the program
	 */
	protected IntCodeState runprogram( ) throws ICERuntimeException {
		logger.info( "Starting/resuming program execution" );
		state = IntCodeState.Running;

		while( state.isRunning( ) ) {
			if( IP >= program.size( ) ) throw new ICERuntimeException( this, "No more instructions to perform but program did not terminate" );
			
			try {
				// determine instruction from the intcode
				final int icode = program.get( IP++ ).intValue( );
				final InstructionSet instr = InstructionSet.fromIntCode( icode % 100 );
				if( instr == null ) throw new ICEUnsupported( this, "Unsupported instruction: " + icode );
				
				// parse arguments, make sure last argument is always in position mode
				final int argcount = instr.getArgumentCount( );
				Argument[] args = null;
				if( argcount > 0 ) {
					args = new Argument[ argcount ];
					
					// get addressing modes, first pad instruction code with zeroes then strip the instruction 
					String addressing = StringUtil.reverse( "" + icode );
					while( addressing.length( ) < 2 + argcount ) addressing += "0";
					addressing = addressing.substring( 2 );
					
					// validate arguments and mode counts
					if( args.length != addressing.length( ) ) 
						throw new ICERuntimeException( this, "The parameter mode '" + addressing + "' does not corresponds to the parameter count " + args.length + " for instruction " + instr + " (" + icode + ")");
					
					for( int i = 0; i < args.length; i++ )
						args[i] = new Argument( program.get( IP++ ), addressing.charAt( i ) );
				}
	
				logger.debug( "Instruction at " + (IP - 1) + ": " + instr + (args != null ? " (" + StringUtil.fromArray( args ) + ")" : "") );
				
				// execute instruction 
				instruction = Instruction.getInstruction( instr, args );
				instruction.execute( this );
			} catch( ICEInvalidSyntax is ) {
				io.close( );

				// catch definition errors and append with runtime information
				throw new ICEUnsupported( this, is.getMessage( ) );
			}
		}
		
		// terminated successfully
		logger.info( "Program ended in state " + state );
		return state;
	}
	
	/**
	 * Ends the current program nicely
	 * 
	 * @throws ICEInvalidState if the program was not expecting an end instructiuon 
	 */
	public void end( ) throws ICEInvalidState {
		ICEInvalidState.throwIfNotValid( this, IntCodeState.Running, "Program was not running" );
		this.state = IntCodeState.Ended;
		io.close( );
	}
	
	/**
	 * Terminate the running IntCode program, can be used only when the program
	 * is running in a threaded mode
	 * 
	 * @param silent True to silence all termination errors
	 */
	public void halt( final boolean silent ) {
		logger.info( "Halting program in current state " + state );
		if( !silent ) {
			io.close( );
			this.state = IntCodeState.Halted;
		} else {
			try {
				io.close( );
				this.state = IntCodeState.Halted;
			} catch( Exception e ) {
				logger.debug( "Caught exception: " + e.toString( ) );
			}
		}		
	}
	
	/**
	 * Loads a value form the program memory
	 * 
	 * @param index The memory index to read from or value in immediate mode
	 * @param mode The memory addressing mode to use
	 * @return The integer value at the index
	 * @throws ICEMemoryInvalid if the indexed memory is out of bounds
	 */
	public long load( final long index, final AddressingMode mode ) throws ICEMemoryInvalid {		
		final long value;
		// immediate mode? return index as the value
		if( mode == AddressingMode.Immediate ) {
			value = index;
		} else {
			// compute target address and make sure memory is sufficiently big
			final int target = (int)index + (mode == AddressingMode.Relative ? MEMBASE : 0);
			if( target < 0 ) throw new ICEMemoryInvalid( this, index );
			while( target >= program.size( ) ) program.add( (long)0 );
			
			if( program.size( ) <= target )
				throw new ICEMemoryInvalid( this, target );
			
			// now return its value
			value = program.get( target );
		}
		logger.debug( "Loading memory at index " + index + ": " + value + " (mode " + mode + ")" );
		return value;
	}
	
	/**
	 * Stores a value in the program memory
	 * 
	 * @param index The index to store the value at
	 * @param mode The memory addressing mode to use
	 * @param value The value to store
	 * @return The previous value at the index
	 * @throws ICEMemoryInvalid if the addressed memory is out of bounds
	 * @throws ICEUnsupported if the immediate addressing mode is used in store
	 */
	public long store( final long index, final AddressingMode mode, final long value ) throws ICEMemoryInvalid, ICEUnsupported {
		// check immediate mode 
		if( mode == AddressingMode.Immediate )
			throw new ICEUnsupported( this, "Immediate addressing mode not supported in store operation" );
		
		// compute target address and make sure memory is sufficiently big
		final int target = (int)index + (mode == AddressingMode.Relative ? MEMBASE : 0);
		if( target < 0 ) throw new ICEMemoryInvalid( this, index );
		while( target >= program.size( ) ) program.add( (long)0 );

		final long prev = program.get( target );
		program.set( target, value );
		logger.debug( "Storing memory at index " + target + ": " + value + " (mode " + mode + ", previous value " + prev + ")" );
		return prev;
	}
	
	/**
	 * Changes the memory base offset to the specified value
	 * 
	 * @param offset The new memory base offset value
	 */
	public void offset( final int offset ) {
		logger.debug( "Setting memory base to " + (MEMBASE + offset) + ", was " + MEMBASE );
		this.MEMBASE += offset;
	}
	
	/**
	 * Inputs a single value
	 * 
	 * @return The value read from the input stream
	 * @throws ICEInputOutput if the input stream failed
	 * @throws ICEYield if there is no input and the yield mode is set to true
	 */
	public long input( ) throws ICEInputOutput, ICEYield {
		if( !threaded && !io.hasInput( ) ) {
			logger.info( "No input available, yielding program execution" );
			// reset IP to the input instruction so that execution is resumed from there
			IP = IP - InstructionSet.INPUT.getArgumentCount( ) - 1;
			state = IntCodeState.AwaitingInput;
			throw new ICEYield( this );
		}
		
		final long value = io.read( );
		logger.debug( "Input received value " + value );
		return value;
	}
	
	/**
	 * Outputs a single value
	 * 
	 * @param value The value to output
	 */
	public void output( final long value ) {
		logger.debug( "Output writes value " + value );
		io.write( value );
	}
	
	/**
	 * Jumps to the specified index
	 * 
	 * @param index The index to set the IP to
	 * @throws ICEMemoryInvalid if the jump target index is invalid
	 */
	public void jump( final int index ) throws ICEMemoryInvalid {
		logger.debug( "Jumping to index " + index );
		if( index < 0 || index >= program.size( ) ) throw new ICEMemoryInvalid( this, index );
		this.IP = index;
	}
		
	
	
	
	/**
	 * Parses a program from a comma separated string
	 * 
	 * @param name The program name
	 * @param prog The program string
	 * @return The program as an IntCode
	 */
	public static IntCode parse( final String name, final String prog ) {
		final List<Long> program = new ArrayList<>( );
		for( long i : StringUtil.toLongArray( prog ) ) program.add( i );		
		return new IntCode( name, program );
	}
	
	/**
	 * Returns the program listing up to and including the terminate instruction
	 *   
	 * @return The current IntCode program as a string */
	public String getProgram( ) {
		String res = "";
		for( long i : program ) {
			res += i + ",";
			if( i == 99 ) break;
		}
		return res.substring( 0, res.length( ) - 1 );
	}
	
	/**
	 * Returns the program listing from the terminate instruction onward (exclusive)
	 *   
	 * @return The current IntCode program as a string */
	public String getMemory( ) {
		String res = "";
		boolean seenend = false;
		for( long i : program ) {
			if( i == 99 ) seenend = true;
			if( !seenend ) continue;
			res += i + ",";
		}
		return res.substring( 0, res.length( ) - 1 );
	}
	
	/**
	 * Returns the full program listing, including extra memory addresses after the terminate instruction
	 *  
	 * @return The current IntCode program as a string
	 */
	public String getFullProgram( ) {
		String res = "";
		for( long i : program ) res += i + ",";
		return res.substring( 0, res.length( ) - 1 );
	}	

	/** @return The IO manager */
	public IOManager getIO( ) {
		return this.io;
	}
	
	/** @return The program's current IntCode listing, 8 codes per line */
	@Override
	public String toString( ) {
		String res = "";
		for( int i = 0; i < program.size( ); i++ ) {
			if( i % 8 == 0 ) res += "[" + String.format( "%4d", i ) + "]   "; 
			res += String.format( "%6d", program.get( i ) ) + ",";
			if( (i+1) % 8 == 0 ) res += "\n";
		}
		return res;
	}


	
	
	/**
	 * Runs the program in a separate thread
	 * 
	 * @throws ICERuntimeException if the program failed to start
	 */
	public void runThreaded( ) throws ICERuntimeException {
		final IntCode self = this;
		final Thread thread = new Thread( new Runnable( ) {
			@Override
			public void run( ) {
				try {
					self.threaded = true;
					self.run( );
				} catch( ICERuntimeException e ) {
					e.printStackTrace();
				}
			}
		}, name );
		thread.start( );
		
		// wait a little until the program is running
		long wait = 500;
		while( !self.isRunning( ) && !self.isWaiting( ) ) {
			if( wait < 0 ) throw new ICERuntimeException( self, "Failed to activate program (state: " + self.state + ")"  );
			try {
				Thread.sleep( 10 );
				wait -= 10;
			} catch( InterruptedException e ) { /* do nothing */ }
		}
	}
	
	/**
	 * Feeds a single value to the intcode program
	 * 
	 * @param value The value to feed to the program 
	 * @throws ICERuntimeException if the program was not expecting any input
	 */
	public void feed( final long value ) throws ICERuntimeException {
		ICEInvalidState.throwIfNotValid( this, "Can only feed input to a running or yielded program", IntCodeState.Running, IntCodeState.AwaitingInput );
		io.feed( value );
	}
	
	/**
	 * Inputs a single value and awaits its processing
	 * 
	 * @param value The input value
	 * @throws ICERuntimeException if the program was not expecting any input
	 */
	public void awaitInput( final long value ) throws ICERuntimeException {
		feed( value );
		io.awaitReader( );
	}
	
	/**
	 * Consumes a single output value
	 * 
	 * @return The value that was consumed
	 * @throws ICEInputOutput if reading the output failed
	 */
	public long consume( ) throws ICEInputOutput {
		return io.consume( );
	}
	
	/**
	 * Consumes multiple output values
	 * 
	 * @param n The number of output values to consume
	 * @return The array of consumed values
	 * @throws ICEInputOutput if there is no output available within the maximum time limit
	 */
	public long[] consume( final int n ) throws ICEInputOutput {
		final long[] out = new long[ n ];
		for( int i = 0; i < out.length; i++ ) out[i] = io.consume( );
		return out;
	}
	
	/**
	 * Awaits a number of output values of the program
	 * 
	 * @param count The number of outputs to wait for
	 * @return The output values as an array
	 * @throws ICEInputOutput if the waiting timed out
	 */
	public long[] awaitOutput( final int count ) throws ICEInputOutput {
		io.awaitWriter( count );
		final long[] result = new long[ count ];
		for( int i = 0; i < result.length; i++ ) result[i] = io.consume( );
		return result;
	}
}
