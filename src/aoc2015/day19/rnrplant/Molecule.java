package aoc2015.day19.rnrplant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A molecule that combines several atoms 
 * 
 * @author Joris
 */
public class Molecule implements Iterable<Atom> {
	/** The atoms in this molecule, ordered left to right */
	private final List<Atom> atoms;
	
	/** The pre-computed hash code */
	private final int hash;
	
	/**
	 * Creates a new molecule that only holds a single atom
	 * 
	 * @param atom
	 */
	public Molecule( final Atom atom ) {
		this.atoms = new ArrayList<>( );
		atoms.add( atom );
		this.hash = toString( ).hashCode( );
	}
	
	/**
	 * Creates a new molecule with the initial collection of atoms
	 * 
	 * @param atoms The initial atoms in the molecule 
	 */
	public Molecule( final Collection<Atom> atoms ) {
		this.atoms = new ArrayList<>( atoms );
		this.hash = toString( ).hashCode( );
	}
	
	/**
	 * Generates all new molecules that may be generated through one step of the
	 * fission process for each of its atoms
	 * 
	 * @param fissionrules The map of fission rules per atom
	 * @param steps The number of fission steps to perform
	 * @return The set of unique molecules produced in the fission process 
	 */
	public Set<Molecule> fission( final Map<Atom, List<Molecule>> fissionrules ) {
		Set<Molecule> results = new HashSet<>( );
		
		for( int i = 0; i < getAtoms( ).size( ); i++ ) {
			// build a new molecule by splitting the atom in the molecule
			final List<Atom> newatoms = new ArrayList<>( getAtoms( ) );
			final Atom atom = newatoms.remove( i );
			
			// are there replacement rules for this atom?
			if( !fissionrules.containsKey( atom ) ) {
				// no, just add the already existing molecule
				results.add( this );
				continue;
			}
				
			// yes, insert all atoms of the replacement molecule in the new atom list
			for( final Molecule m : fissionrules.get( atom ) ) {
				final List<Atom> newmolecule = new ArrayList<>( newatoms );
				newmolecule.addAll( i, m.getAtoms( ) );
				results.add( new Molecule( newmolecule ) );
			}
		}
			
		return results;
	}
	
	/**
	 * Generates all new molecules that may be generated through one step of the
	 * fusion process for possible sub molecules
	 * 
	 * @param fissionrules The map of fission rules per atom (fusion is fission
	 *   reversed)
	 * @return The set of unique molecules produced in the fusion process 
	 */
	
	public List<Molecule> fusion( final Map<Atom, List<Molecule>> fissionrules ) {
		List<Molecule> results = new ArrayList<>( );
		
		// try to reverse each of the fission rules
		for( final Atom resatom : fissionrules.keySet( ) ) {			
			// check if any of the result molecules is present in this one
			for( final Molecule resmol : fissionrules.get( resatom ) ) {
				final String molstring = toString( );
				final String repstring = resmol.toString( );
				
				if( !molstring.contains( repstring.toString( ) ) ) continue;
				if( resatom.isE( ) && molstring.length( ) != repstring.length( ) ) continue;
				
				// yes, the result molecule is present, perform fusion for every occurrence
				// use sneaky string replacement to substitute instead of searching and 
				// modifying the atom array itself
				int idx = -1;
				while( (idx = molstring.indexOf( repstring, idx + 1)) != -1) {
					final Molecule newmol = Molecule.fromString( molstring.substring( 0, idx ) + resatom + molstring.substring( idx + repstring.length( ) ) );
					if( !results.contains( newmol ) ) results.add( newmol );
					idx += repstring.length( );
				}
			}			
		}
		
		return results;
	}
	
	/** @return True if this molecule only consists of the e atom */
	public boolean isE( ) {
		return atoms.size( ) == 1 && atoms.get( 0 ).isE( );
	}
	
	/** @return The set of atoms */
	public List<Atom> getAtoms( ) {
		return atoms;
	}
	
	/** @return The iterator over the atom collection */
	@Override
	public Iterator<Atom> iterator( ) {
		return atoms.iterator( );
	}
	
	/**
	 * Joins the two molecules, returns a new molecule that contains the atoms
	 * of this molecule first, followed by that of the second molecule
	 * 
	 * @param mol The other molecule to add
	 * @return A new molecule that is the combination of both molecules
	 */
	public Molecule join( final Molecule mol ) {
		final List<Atom> combination = new ArrayList<>( atoms );
		combination.addAll( mol.atoms );
		return new Molecule( combination );
	}
	
	/** @return The number of atoms in this molecule */
	public int size( ) {
		return atoms.size( );
	}
	
	/**
	 * Checks if this molecule equals another one
	 * 
	 * @param obj The object to test against
	 * @return True iff the obj is a valid molecule and it contains the same
	 *   atoms in the same order
	 */
	@Override
	public boolean equals( Object obj ) {
		if( obj == null || !(obj instanceof Molecule) ) return false;
		final Molecule m = (Molecule)obj;
		
		// simply compare pre-computed hash codes, they must be equal for unique lists of atoms
		return hashCode( ) == m.hashCode( );
	}
	
	/** @return The hash code of the string that is a unique configuration of atoms */
	@Override
	public int hashCode( ) {
		return hash;
	}
	
	/**
	 * Parses a string of atoms into a definition of a molecule
	 * 
	 * @param input The string that describes the atoms
	 * @return The molecule
	 */
	public static Molecule fromString( final String input ) {			
		final Matcher m = Pattern.compile( "([A-Z][a-z]?|e)" ).matcher( input );
		final List<Atom> atoms = new ArrayList<>( );
		while( m.find( ) ) {
			atoms.add( new Atom( m.group( 1 ) ) );
		}
		return new Molecule( atoms );
	}
	
	/** @return The atoms in this molecule */
	@Override
	public String toString( ) {
		if( atoms.size( ) == 0 ) return "";
		final StringBuilder res = new StringBuilder( );
		for( final Atom a : atoms ) res.append( a.toString( ) );
		return res.toString( );
	}
}
