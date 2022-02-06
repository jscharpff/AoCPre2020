package aoc2018.day04;

/**
 * Keeps track when a Guard sleeps during its shift
 *  
 * @author Joris
 */
public class Guard {
	/** The ID of the Guard */
	protected final int ID;
	
	/** The number of times he sleeps per minute of his shift */
	protected final int[] sleeping;
	
	/** Total sleeping time */
	protected int sleeptime;
	
	/**
	 * Creates a new Guard shift
	 * 
	 * @param ID The Guards ID
	 */
	public Guard( final int ID )  {
		this.ID = ID;
		this.sleeping = new int[ 61 ];
		this.sleeptime = 0;
	}
	
	/** @return The ID of the Guard */
	public int getID( ) { return this.ID; }
	
	/** @return The total sleep time */
	public int getSleepTime( ) { return this.sleeptime; }
	
	/**
	 * Adds the sleep interval
	 * 
	 * @param start The minute at which the guard falls asleep (inclusive)
	 * @param end The minute in which the guard wakes up (non-inclusive) 
	 */
	public void addSleepWindow( final int start, final int end ) {
		for( int i = start; i < end; i++ ) sleeping[i]++;
		sleeptime += (end - start) - 1;
	}
	
	/**
	 * Finds the highest sleep count over all minutes
	 * 
	 * @return The count of times that the guard was sleeping as most
	 */
	public int getMaxSleepCount( ) {
		int max_count = -1;
		for( int i = 0; i < sleeping.length; i++ )
			if( sleeping[i] > max_count ) max_count = sleeping[i];
		
		return max_count;	}
	
	/**
	 * Finds the minute at which the guard sleeps most often
	 * 
	 * @return The minute
	 */
	public int getMostSleepedAt( ) {
		int max_time = 0;
		for( int i = 0; i < sleeping.length; i++ )
			if( sleeping[i] > sleeping[max_time] ) max_time = i;
		
		return max_time;
	}
	
	@Override
	public int hashCode( ) {
		return ID;
	}
	
	@Override
	public boolean equals( Object obj ) {
		if( obj == null || !(obj instanceof Guard) ) return false;
		return ((Guard)obj).ID == ID;
	}
	
	@Override
	public String toString( ) {
		String r = "Guard " + ID + ":";
		for( int i = 0; i < sleeping.length; i++ )
			r += " " + sleeping[i];
		return r;
	}
}
