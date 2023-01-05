package aoc2019.intcode.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Simple text window for simple grid-like output
 * 
 * @author Joris
 */
public class TextWindow {
	/** The frame that is used */
	protected final JFrame frame;
	
	/** The main text panel used for output */
	protected final JTextArea textarea;
	
	/** The text panel used for input */
	protected final JTextField textinput;
	
	/** The mode of outputting new text, false for clear true for append */
	protected boolean appendmode = false;
	
	/**
	 * Creates a new text window, using a JFrame as its dialog
	 * 
	 *  @param title The window title
	 */
	public TextWindow( final String title ) {
		frame = new JFrame( title );
		frame.setSize( 600, 300 );
		frame.setLocation( 400, 100 );
		
		// create panel and input/output text fields
		final JSplitPane panel = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
		textarea = new JTextArea( 60, 120 );
		textarea.setText( "" );
		textarea.setFont( new Font( "Courier New", Font.PLAIN, 12 ) );
		
		textinput = new JTextField( "" );
		textinput.setFont( new Font( "Courier New", Font.PLAIN, 12 ) );
		
		panel.add( new JScrollPane( textarea ) );
		panel.add( textinput );
		
		// add to frame
		frame.getContentPane( ).add( panel );
		frame.pack( );
		
		frame.addWindowListener( new WindowAdapter( ) {
			public void windowClosing(java.awt.event.WindowEvent e) { unload( ); };
		} );
	}
	
	/**
	 * Shows the window
	 */
	public void show( ) {
		frame.setVisible( true );
		textinput.requestFocus( );
	}
	
	/**
	 * Hides the frame
	 */
	public void hide( ) {
		frame.setVisible( false );
	}
	
	/** @return True if the frame is visible */
	public boolean isVisible( ) {
		return frame != null && frame.isVisible( );
	}
	
	/**
	 * Unloads and terminates the frame
	 */
	public void unload( ) {
		hide( );
		frame.dispose( );
	}
	
	/**
	 * Enables/disables append mode
	 * 
	 * @param en True to enable append mode
	 */
	public void setAppendMode( final boolean en ) {
		appendmode = en;
	}
	
	/**
	 * Sets the text to be displayed
	 * 
	 * @param text The text to display
	 */
	public void setText( final String text ) {
		if( appendmode ) {
			// append text and move caret position to end of text to force scrolling
			textarea.append( text );
			textarea.moveCaretPosition( textarea.getText( ).length( ) );
		} else {
			// simply overwrite the text
			textarea.setText( text );
		}
		
		textinput.requestFocus( );
	}
	
	/** @return The reference to the frame, for setting options etc */
	public JFrame getFrame( ) { return frame; }
	
	/**
	 * Appends a single character to the current output
	 * 
	 * @param c The character to write
	 */
	public void write( final char c ) {
		textarea.setText( textarea.getText( ) + c );
	}
	
	/**
	 * Appends a string to the current output
	 * 
	 * @param string The string to write
	 */
	public void write( final String string ) {
		textarea.setText( textarea.getText( ) + string );
	}
	
	/**
	 * Clears the text
	 */
	public void clear( ) {
		textarea.setText( "" );
	}
	
	/**
	 * Adds an action listener to the text input field
	 * 
	 * @param consumer The function that handles the input
	 */
	public void addInputListener( final Consumer<String> consumer ) {
		textinput.addActionListener( new java.awt.event.ActionListener( ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				consumer.accept( textinput.getText( ) );
				textinput.setText( "" );
			}
		});
	}
}
