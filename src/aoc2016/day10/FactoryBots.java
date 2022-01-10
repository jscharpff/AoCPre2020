package aoc2016.day10;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aocutil.object.LabeledObject;

public class FactoryBots {
	/** The bots in the factory, by ID */
	private final Map<Integer, Bot> bots;
	
	/** The output bins */
	private final Map<Integer, Integer> outputs;
	
	/**
	 * Creates a new FactoryBots controller
	 */
	public FactoryBots( ) {
		 bots = new HashMap<>( );
		 outputs = new HashMap<>( );
	}
	
	/**
	 * Finds the bot that compares the given chip numbers
	 * 
	 * @param low The low valued chip
	 * @param high The high valued chips
	 * @return The ID of the bot that compares the chip numbers
	 */
	public int getBotHandling( final int low, final int high ) {
		if( bots.size( ) == 0 ) throw new RuntimeException( "Bots have not been initialised, run process first!" );
		
		for( final Bot b : bots.values( ) )
			if( b.hasChips( low, high ) )
				return b.ID;
		
		throw new RuntimeException( "Now bot holds the specified chips" );
	}
	
	/**
	 * Retrieves the values at the specified output bins
	 * 
	 * @param bins The numbers of the output bins to retrieve
	 * @return The list of outputs contained by the bins 
	 */
	public List<Integer> getOutputs( final int... bins ) {
		final List<Integer> out = new ArrayList<>( bins.length );
		for( final int b : bins ) out.add( outputs.get( b ) );
		return out;
	}
	
	
	/**
	 * Processes the list of instructions to the robots
	 * 
	 * @param input The list of instructions to process
	 */
	public void process( final List<String> input ) {
		final List<String> remaining = new ArrayList<>( input );

		// first scan all initialisation commands to set up the robot factory
		for( int i = remaining.size( ) - 1; i >= 0; i-- ) {
			final Matcher m = Pattern.compile( "value (\\d+) goes to bot (\\d+)" ).matcher( remaining.get( i ) );
			if( m.matches( ) ) {
				final int botID = Integer.parseInt( m.group( 2 ) );
				if( !bots.containsKey( botID ) ) bots.put( botID, new Bot( botID ) );
				bots.get( botID ).receive( Integer.parseInt( m.group( 1 ) ) );
				remaining.remove( i );
			} 
		}
		
		// then process commands for the bots, remove processed commands
		while( remaining.size( ) > 0 ) {
			for( int i = remaining.size( ) - 1; i >= 0; i-- ) {
				if( processGiveCommand( remaining.get( i ) ) ) remaining.remove( i );
			}
		}
	}
	
	/** Cached pattern for command processing */
	private final Pattern PAT_GIVECMD = Pattern.compile( "bot (\\d+) gives low to (output|bot) (\\d+) and high to (bot|output) (\\d+)" );
	
	/**
	 * Processes a single give instruction
	 * 
	 * @param cmd The instruction to process
	 * @return True if the instruction was processed successfully 
	 */
	private boolean processGiveCommand( final String cmd ) {
		// parse the command
		final Matcher m = PAT_GIVECMD.matcher( cmd );
		if( !m.find( ) ) throw new RuntimeException( "Invalid give command: " + cmd );
		
		// valid, now process it if possible
		final Bot b = bots.get( Integer.parseInt( m.group( 1 ) ) );
		if( b == null || !b.canGive( ) ) return false;
		
		for( final int i : new int[] { 2, 4 } ) {
			final int ID = Integer.parseInt( m.group( i + 1 ) );
			final int chip = b.give( i == 2 );
			
			if( m.group( i ).equals( "output" ) ) {
				outputs.put( ID, chip );
			} else {
				if( !bots.containsKey( ID ) ) bots.put( ID, new Bot( ID ) );
				bots.get( ID ).receive( chip );
			}
		}
		
		return true;
	}
	
	
	/**
	 * @return The map of bots and outputs
	 */
	@Override
	public String toString( ) {
		return bots.values( ).toString( ) + "\n" + outputs.toString( );
	}
	
	
	/**
	 * The class that models a bot that holds at most two values
	 */
	private class Bot extends LabeledObject {
		/** The ID of the bot */
		private final int ID;
		
		/** The value of the chips it holds */
		private List<Integer> values;
		
		/**
		 * Creates a new bot with the specified ID
		 * 
		 * @param ID The bot ID
		 */
		private Bot( final int ID ) {
			super( "Bot " + ID );
			this.ID = ID;

			values = new ArrayList<>( 2 );
		}
		
		/** @return True iff the robot can receive a chip */
		public boolean canReceive( ) { return values.size( ) < 2; }
		
		/**
		 * Gives a chip to the bot
		 * 
		 * @param chip The chip with the given number
		 */
		public void receive( final int chip ) {
			if( !canReceive( ) ) throw new RuntimeException( "The bot already holds two chips" );
						
			// add the chip and keep the arraylist sorted so that the lowest chip ID
			// is the first element
			values.add( chip );
			values.sort( Integer::compareTo );
		}
		
		/** @return True iff the robot can give a chip */
		public boolean canGive( ) { return values.size( ) == 2; }
		
		
		/**
		 * Takes the lowest / highest value chip from this bot
		 * 
		 * @param lowest True for lowest, false for highest
		 * @return The lowest/highest value chip
		 */
		public int give( final boolean lowest ) {
			if( values.size( ) != 2 ) throw new RuntimeException( "The bot has not yet received two chips" );			
			return values.get( lowest ? 0 : 1 );
		}
		
		/**
		 * Checks if the bot has the specified chips
		 * 
		 * @param chips The chip IDs
		 * @return True if the bot has the chip numbers
		 */
		public boolean hasChips( final int... chips ) {
			if( chips.length > 2 ) return false;
			
			for( final int c : chips )
				if( !values.contains( c ) ) return false;
			return true;
		}
		
		
		/** @return The bot as a string */
		@Override
		public String toString( ) {
			return super.toString(  ) + ": " + values.toString( );
		}
	}

}
