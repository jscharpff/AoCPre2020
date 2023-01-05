package aoc2019.day25.droids;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Stream;

import aoc2019.intcode.IntCode;
import aoc2019.intcode.IntCodeMachine;
import aoc2019.intcode.exceptions.ICEInputOutput;
import aoc2019.intcode.exceptions.ICERuntimeException;
import aocutil.collections.CollectionUtil;
import aocutil.geometry.Coord2D;
import aocutil.geometry.Direction;
import aocutil.object.LabeledObject;
import aocutil.string.RegexMatcher;

public class AutoDroid extends IntCodeMachine {
	/** The map it has explored so far */
	protected Map<Room, List<Direction>> rooms;
	
	/** The droid inventory */
	protected final Set<Item> inventory;
	
	/**
	 * Initialises the automatic droid
	 * 
	 * @param program The program to run it with
	 */
	public AutoDroid( final String program ) {
		super( IntCode.parse( "AutoDroid", program ), false );
				
		getProgram( ).getIO( ).setOutputEnabled( false );
		
		// initialise search
		rooms = new HashMap<>( );
		inventory = new HashSet<>( );
	}
		
	
	/**
	 * Starts the droid and will run the solving algorithm until the password is
	 * found
	 *
	 * @return The password
	 * @throws ICERuntimeException 
	 */
	public String run( ) throws ICERuntimeException {
		// activate the program
		activate( );
		
		// first explore the map of the space ship and collect all items
		System.out.print( "Exploring the space ship..." );
		explore( new Stack<>( ) );
		System.out.println( "done!" );
		
		// then move to the security checkpoint
		final Room checkpoint = getRoom( Room.CheckPoint );
		Coord2D pos = new Coord2D( 0, 0 );
		for( final Direction d : rooms.get( checkpoint ) ) {
			pos = pos.move( d, 1 );
			move( d );
		}
		
		// and brute force the pressure floor open until we have the password
		System.out.print( "Negiotating with pressurised floor..."  );
		final String password = openWithBruteForce( checkpoint );
		System.out.println( "done!" );
		return password;
	}
		
	/**
	 * Retrieves a room object by its name
	 * 
	 * @param name The name of the room
	 * @return The room or null if no such room exists
	 */
	private Room getRoom( final String name ) {
		for( final Room r : rooms.keySet( ) )
			if( r.getLabel( ).equals( name ) ) return r;
		
		return null;
	}
	
	/**
	 * DFS algorithm to explore all unvisited rooms from a given coordinate
	 * 
	 * @param path The path we are exploring
	 */
	private void explore( final Stack<Direction> path ) {
		// parse the room from the output and stop exploring if already known
		final Room room = Room.fromOutput( processOutput( ) );
		if( rooms.containsKey( room ) ) return;
		
		// new room, add it to the list with the path to it from starting pos
		rooms.put( room, new ArrayList<>( path ) );
		
		// pick up all items in this room, with some exceptions due to safety
		// concerns...
		for( final Item item : room.items ) {
			if( !item.safe ) continue;			
			take( room, item );
		}
		
		// special case: pressure sensitive floor, prevent infinite loop here
		// since we do not have all the items yet to test it
		if( room.getLabel( ).equals( Room.PressureFloor ) ) return;
		
		// explore its neighbours
		for( final Direction d : room.neighbours ) {
			// move into the room
			move( d );
			path.push( d );
			
			// explore the new room
			explore( path );
			
			// after exploring move back and discard the output
			path.pop( );
			move( d.flip( ) );
			processOutput( ); /* room already known */
		}
	}

	/**
	 * Brute forces the combination of items required to pass the pressure
	 * sensitive floor. Note: this function is only valid if the droid is in the
	 * security checkpoint room
	 * 
	 * @param checkpoint The security checkpoint room
	 * @return The password or null if the process failed
	 */
	private String openWithBruteForce( final Room checkpoint ) {
		if( !checkpoint.getLabel( ).equals( Room.CheckPoint ) )
			throw new IllegalArgumentException( "The droid should be in the security checkpoint room" );
		
		// determine the move needed to get from the security checkpoint into the
		// pressurised floor
		final List<Direction> m = new ArrayList<>( checkpoint.neighbours );
		final List<Direction> path = rooms.get( checkpoint );
		m.remove( path.get( path.size( ) - 1 ).flip( ) );
		final Direction mdir = m.get( 0 );
		
		// generate all permutations of the items in the inventory
		final List<Set<Item>> perm = CollectionUtil.generateSubSets( new HashSet<>( inventory ) );
		for( final Set<Item> items : perm ) { 
			// drop specified items and try if the weight of remaining items is now
			// exactly right to satisfy the pressurised floor
			for( final Item i : items ) drop( checkpoint, i );
			
			// move into the pressurised floor room and get the response
			move( mdir );
			final List<String> answer = processOutput( );
			if( answer.get( 4 ).contains( "Analysis complete!" ) ) {
				// we've found the right combination of items, extract the security code
				return RegexMatcher.extract( "by typing (#D) on the keypad", answer.get( answer.size( ) - 1 ) );
			}
			
			// re-pack our stuff if we failed and try again with a different
			// combination of items
			for( final Item i : items ) take( checkpoint, i );			
		}
			
		// failed to negotiate with the pressurised floor check
		return null;
	}
	
	/**
	 * Reads all available output from the program
	 * 
	 * @return The string containing all output
	 * @throws ICEInputOutput
	 */
	private List<String> processOutput( ) {
		// get all output text until the output buffer is empty
		final StringBuilder s = new StringBuilder( );
		try {
			while( getProgram( ).getIO( ).hasOutput( ) ) {
				s.append( (char) getProgram( ).consume( ) );
			}
		} catch( ICEInputOutput e ) {
			e.printStackTrace();
		}
		
		// convert into array list, one string per line, while discarding the empty
		// lines in the output
		final List<String> out = new ArrayList<>( );
		for( final String line : s.toString( ).split( "\\n" ) )
			if( !line.equals( "" ) ) out.add( line );
		
		// and return the output
		return out;
	}
	
	/**
	 * Sends a single text command to the program
	 * 
	 * @param command The command to send
	 * @throws ICERuntimeException 
	 */
	private void sendCommand( final String command ) {
		try {
			feedASCII( command );
			getProgram( ).resume( );
		} catch( ICERuntimeException e ) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a command to move in the specified direction
	 * 
	 * @param dir The direction to move to
	 */
	private void move( final Direction dir ) {
		sendCommand( dir.toString( ).toLowerCase( ) );
	}
	
	/**
	 * Picks up an item from the room
	 * 
	 * @param room The room to take the item from
	 * @param item The item to take
	 */
	private void take( final Room room, final Item item ) {
		if( !room.items.contains( item ) )
			throw new IllegalArgumentException( "Cannot pick up item '" + item + "' from room " + room + ": it is not here!" );
		
		// move it from room to inventory
		room.items.remove( item );
		inventory.add( item );
		sendCommand( "take " + item.toString( ) );
		processOutput( ); /* discard output saying we've picked it up */
	}
	
	/**
	 * Drops the item from the inventory in the room
	 *
	 * @param room The room to drop it into
	 * @param item The item to drop
	 */
	private void drop( final Room room, final Item item ) {
		if( !inventory.contains( item ) )
			throw new IllegalArgumentException( "Cannot drop item '" + item + "' into room " + room + ": it is not in the inventory!" );
		
		// move the item from inventory into the room
		inventory.remove( item );
		room.items.add( item );
		sendCommand( "drop " + item.toString( ) );
		processOutput( ); /* discard output saying we've dropped it */
	}
	
	
	/**
	 * Describes a single room in the space ship
	 */
	protected static class Room extends LabeledObject {
		/** The room description text */
		protected final String description;
		
		/** The neighbouring room directions */
		protected final Set<Direction> neighbours; 
		
		/** The items in this room */
		protected final Set<Item> items;
		
		// room labels of special rooms
		protected static String CheckPoint = "Security Checkpoint";
		protected static String PressureFloor = "Pressure-Sensitive Floor";
		
		
		/**
		 * Initialises a new empty room
		 * 
		 * @param name The name of the room
		 * @param desc The room description
		 */
		public Room( final String name, final String desc ) {
			super( name );
			this.neighbours = EnumSet.noneOf( Direction.class );
			this.items = new HashSet<>( );
			this.description = desc;
		}
		
		/**
		 * Reconstructs a room's contents from textual output
		 * 
		 * @param output The output
		 * @return The room
		 */
		protected static Room fromOutput( final List<String> output ) {
			// get room name and description
			final Room r = new Room( RegexMatcher.extract( "== ([\\w\\s-]+) ==", output.get( 0 ) ), output.get( 1 ) );
			
			// parse the directions in which we can travel to adjacent rooms
			// and the items present in this room
			for( int i = 0; i < output.size( ); i++ ) {
				// the doors signify the directions to explore
				if( output.get( i ).equals( "Doors here lead:" ) ) {
					int idx = i;
					while( output.get( ++idx ).startsWith( "- " ) ) {
						r.neighbours.add( Direction.fromString( output.get( idx ).substring( 2 ) ) );
					}
				}
				
				// and parse the list of items present here
				if( output.get( i ).equals( "Items here:" ) ) {
					int idx = i;
					while( output.get( ++idx ).startsWith( "- " ) ) {
						r.items.add( new Item( output.get( idx ).substring( 2 ) ) );
					}
				}

			}

			// return the reconstructed room
			return r;
		}
	}
	
	/**
	 * Simple item that can be picked up and dropped
	 */
	private static class Item extends LabeledObject {
		/** True if the item is safe to pick up, false otherwise */
		protected final boolean safe;
		
		/** Items that are better left alone */
		private static final String[] UNSAFE_ITEMS = new String[] {
			"giant electromagnet", "infinite loop", "photons", "molten lava", "escape pod"
		};
				
		/**
		 * Creates a new item object
		 * 
		 * @param name The name of the item
		 */
		public Item( final String name ) {
			super( name );
			
			// the item is safe to pick up if it is not in the list of unsafe ones
			safe = Stream.of( UNSAFE_ITEMS ).noneMatch( s -> s.equals( name ) );
		}
	}
}
