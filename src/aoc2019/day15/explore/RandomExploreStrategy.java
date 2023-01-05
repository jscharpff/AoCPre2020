package aoc2019.day15.explore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aoc2019.day15.RepairDroid;
import aoc2019.day15.ShipSection;
import aoc2019.intcode.exceptions.ICERuntimeException;
import aocutil.geometry.Coord2D;

public class RandomExploreStrategy extends ExploreStrategy {

	@Override
	public void explore( RepairDroid droid ) throws ICERuntimeException {
		// determine new movement
		while( true ) {
			final int nextmove = next( droid.getPosition( ) );
			if( nextmove == -1 ) break;

			section.tryMove( droid, nextmove );
		}
	}
	
	public int next( final Coord2D coord ) {
		// randomly end
		if( Math.random( ) < 0.01 ) return -1;
		
		// pick a random direction to move towards
		final List<Integer> directions = new ArrayList<>( );
		for( int i = 0; i <= 270; i += 90 ) {
			if( section.getMap().get( coord.moveDir( i, 1 ) ) != ShipSection.TILE_WALL )
				directions.add( i );
		}
		Collections.shuffle( directions );
		directions.add( -1 );
		return directions.get( 0 );
	}

}
