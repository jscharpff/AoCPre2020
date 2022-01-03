package aoc2015.day19.rnrplant;

import aocutil.object.LabeledObject;

/**
 * Represents a single atom
 * 
 * @author Joris
 */
public class Atom extends LabeledObject {
	/**
	 * Creates a new atom
	 * 
	 * @param label The atom's descriptive label
	 */
	public Atom( final String label ) {
		super( label );
	}
	
	/** @return True iff this atom is the special e atom */
	public boolean isE( ) { return label.equals( "e" ); }
}
