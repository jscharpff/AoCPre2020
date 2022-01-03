package aoc2015.day19.rnrplant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Red-Nosed Reindeer Nuclear Fusion/Fission Plant
 * 
 * @author Joris
 */
public class RNRPlant {
	/** The rules for the nuclear fission process */
	private final Map<Atom, List<Molecule>> fissionrules;
	
	/**
	 * Creates a new RNRPlant
	 * 
	 * @param fissionrules The map of nuclear fission rules to use in atom
	 *   substitution
	 */
	private RNRPlant( final Map<Atom, List<Molecule>> fissionrules ) {
		this.fissionrules = fissionrules;
	}
	
	/**
	 * Counts the number of unique molecules that result from performing the
	 * specified number of nuclear fission steps on the given initial molecule
	 *
	 * @param mol The initial molecule
	 * @param best The number of nuclear fission steps to perform
	 * @return The count of unique molecules that can be generated
	 */
	public int countUniqueFission( final Molecule mol ) {
		return mol.fission( fissionrules ).size( );
	}
	
	public int stepsToGenerate( final Molecule target ) {
		final Map<Molecule, Integer> M = new HashMap<>( );
		final StepCounter steps = new StepCounter( );
		return fusion( M, target, steps, 0 );
	}

	private int fusion( final Map<Molecule, Integer> M, final Molecule current, final StepCounter steps, final int currstep ) {
		// have we tried to fuse this molecule before?
		if( M.containsKey( current ) ) {
			// return the current step plus the required extra steps from this molecule onward
			final int cached = M.get( current );
			return cached != Integer.MAX_VALUE ? currstep + cached : cached;
		}
					
		// did we overshoot?
		if( current.size( ) <= 0 || currstep >= steps.best  ) {
			M.put( current, Integer.MAX_VALUE );
			return Integer.MAX_VALUE;
		}
		
		// no, but are we there yet?
		if( current.isE( ) ) {
			System.err.println( "New best solution: " + currstep );
			steps.best = currstep;
			return currstep;
		}
		
		// get all possible new molecules we can generate by nuclear fusion
		// continue fusion process for all new molecules until we found the e atom
		// in the least number of steps
		final List<Molecule> molecules = current.fusion( fissionrules );		
		int minsteps = Integer.MAX_VALUE;
		for( final Molecule mol : molecules ) {
			final int beststeps = fusion( M, mol, steps, currstep + 1 );
			if( beststeps < minsteps ) minsteps = beststeps;			
		}
		
		// update global minimum if possible
		if( minsteps < steps.best ) steps.best = minsteps;
		
		// store the result we have found for future reuse
		M.put( current, minsteps );
		return minsteps;
	}
	
	
	/**
	 * Builds a new RNRPlant from a list of strings that describe the fission
	 * rules
	 * 
	 * @param input The list of fission rules
	 * @return The RNRPlant
	 */
	public static RNRPlant fromStringList( final List<String> input ) {
		// build a map of rules
		final Map<Atom, List<Molecule>> rules = new HashMap<>( input.size( ) );
		
		// parse the rules
		for( final String rule : input ) {
			final String[] r = rule.split( " => " );
			
			// get the atom on the left hand side
			final Atom atom = new Atom( r[0] );
			final List<Molecule> atomrules = rules.getOrDefault( atom, new ArrayList<>( ) );
			atomrules.add( Molecule.fromString( r[1] ) );
			rules.put( atom, atomrules );
		}
		
		return new RNRPlant( rules );
	}
	
	/** @return The current RNRPlant ruleset */
	@Override
	public String toString( ) {
		return fissionrules.toString( );
	}
	
	private class StepCounter {
		/** The number of steps required */
		protected int best = Integer.MAX_VALUE;
	}
}
