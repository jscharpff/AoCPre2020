package aoc2015.day11;

/**
 * Class to generate a new password from an existing one such that it is the
 * next one that adheres to all the password policy rules
 * 
 * @author Joris
 */
public class PasswordGenerator {

	/**
	 * Generates the next valid password from a given current password
	 * 
	 * @param current The current password
	 * @return The next valid password where next is defined as the one with
	 *   minimal character increments from the current password
	 */
	public static String getNextPassword( final String current ) {
		final char[] curr = current.toCharArray( );
		
		while( true ) {
			// increment password by one character
			int idx = curr.length - 1; 
			curr[idx]++;
			while( curr[idx] > 'z' ) {
				if( idx == 0 ) throw new RuntimeException( "Failed to generate next password" );
				curr[idx] = 'a';
				curr[--idx]++;
			}
			
			// check the validity of the password
			if( isValid( curr ) ) return String.valueOf( curr );
		}
	}
	
	/**
	 * Checks if the password is valid, i.e. it meets the security criteria
	 * 
	 * @param pwd The password to check
	 * @return True iff the password is valid
	 */
	private static boolean isValid( final char[] pwd ) {
		// check all password requirements
		// 1) passwords must contain at least one straight of increasing characters
		boolean straight = false;
		for( int i = 0; i < pwd.length - 3; i++ )
			straight |= (pwd[i] == pwd[i+1] - 1 && pwd[i] == pwd[i+2] - 2);
		if( !straight ) return false;
		
		// 2) they may not contain an i, o or l
		final String forbidden = "iol"; 
		for( int i = 0; i < pwd.length - 1; i++ )
			if( forbidden.contains( "" + pwd[i] ) ) return false;
		
		// 3) they must contain at least 2 different, non-overlapping pairs
		char prev = '-';
		int pairs = 0;
		for( int i = 0; i < pwd.length; i++ ) {
			if( pwd[i] == prev ) {
				pairs++;
				prev = '-';
			} else {
				prev = pwd[i];
			}
		}
		if( pairs < 2 ) return false;
		
		// all checks passed!
		return true;
	}
}
