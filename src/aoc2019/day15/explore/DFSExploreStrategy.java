package aoc2019.day15.explore;

import aoc2019.day15.RepairDroid;
import aoc2019.day15.ShipSection;
import aoc2019.intcode.exceptions.ICERuntimeException;
import aocutil.geometry.Coord2D;

public class DFSExploreStrategy extends ExploreStrategy {
	@Override
	public void explore( RepairDroid droid ) throws ICERuntimeException {
		// check all unexplored directions
		for( int dir = 0; dir <= 270; dir += 90 ) {
			if( isUnexplored( droid.getPosition( ), dir ) ) {
				if( section.tryMove( droid, dir ) ) {
					// explore in the direction and move back to the previous position afterwards
					explore( droid );
					section.tryMove( droid, (dir + 180) % 360 );					
				}
			}
		}
	}

	protected boolean isUnexplored( final Coord2D pos, int dir ) {
		return section.getMap( ).get( pos.moveDir( dir, 1 ) ) == ShipSection.TILE_UNEXPLORED;
	}
}
