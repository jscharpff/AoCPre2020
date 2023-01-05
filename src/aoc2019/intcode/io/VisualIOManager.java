package aoc2019.intcode.io;

import aoc2019.intcode.IntCode;
import aoc2019.intcode.ui.TextWindow;
import aocutil.Util;

public class VisualIOManager extends IOManager {
	/** The text window for visual output */
	protected final TextWindow display;
		
	/** Current display buffer (visualised on double newline) */
	protected StringBuilder buffer;
	
	/** Run in ASCII or IntCode mode */
	protected final boolean asciimode;
	
	/** The time in msec to sleep between each visualisation */
	protected static final long DISPLAY_SLEEP = 200;

	/**
	 * Creates a new VisualIOManager that uses a text window for its output
	 * 
	 * @param program The reference to the IntCode program
	 * @param title The title of the window
	 * @param asciimode True to enable ASCII mode, i.e. int values are translated
	 *   into their ASCII encoded equivalents
	 */
	public VisualIOManager( final IntCode program, final String title, final boolean asciimode ) {
		super( program );
		this.asciimode = asciimode;
		this.setOutputEnabled( false );

		buffer = new StringBuilder( );
		
		display = new TextWindow( title );
		display.show( );
	}

	/** @return The display used */
	public TextWindow getDisplay( ) {
		return display;
	}
	
	/**
	 * Outputs the character to the screen, also visualises the character in the
	 * text window if it is a valid ASCII
	 * 
	 * @param value The value to output
	 */
	@Override
	public void write( long value ) {
		// perform regular output and only visualise if the screen is still active
		super.write( value );
		if( !display.isVisible( ) ) return;
		
		// process character
		if( asciimode ) {
			if( value < 0 && value > 255 ) return;
			final char c = (char)value;		
			buffer.append( c );
			if( c == '\n' && buffer.length( ) > 1 && buffer.charAt( buffer.length( ) - 2 ) == '\n' ) {
				// clear output buffer as well
				outbuffer.clear( );
				display.setText( buffer.toString( ) );
				buffer = new StringBuilder( );
				Util.sleep( DISPLAY_SLEEP );
			}
		} else {
			buffer.append( value + "," );
			display.setText( buffer.toString( ) );
		}
	}

	/**
	 * Disposes of the frame
	 */
	@Override
	public void close( ) {
		super.close( );
		
		display.write( "\n\nProgram terminated" );
	}
}
