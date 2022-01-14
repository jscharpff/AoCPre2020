package aoc2017.day21;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;
import aocutil.object.LabeledObject;

/**
 * Class to generate a fractal image using a ruleset
 * 
 * @author Joris
 */
public class FractalArt {
	/** The rule set */
	private List<FractalRule> rules;
	
	/** The current image of the fractal art program as pixels in a grid */
	private CoordGrid<Boolean> pixels;
	
	/**
	 * Creates a new Fractal Art image
	 * 
	 * @param ruleset The set of enhancement rules
	 */
	public FractalArt( final List<String> ruleset ) {
		pixels = new FractalPattern( ".#./..#/###" ).toGrid( );
		rules = new ArrayList<>( ruleset.stream( ).map( FractalRule::new ).toList( ) );
	}
	
	/**
	 * Performs the number of art generation rounds, using the given enhancement
	 * rules
	 * 
	 * @param rounds The number of generation rounds
	 */
	public void generate( final int rounds ) {
		for( int i = 0; i < rounds; i++ )	{
			final int sqsize = pixels.size( ).x % 2 == 0 ? 2 : 3;			
			pixels = generate( pixels, sqsize );
		}
	}
	
	/**
	 * Performs a single step of the fractal art generation algorithm
	 * 
	 * @param image The image to generate the next step for
	 * @param sqsize The square size to use
	 * @return The new image
	 */
	private CoordGrid<Boolean> generate( final CoordGrid<Boolean> image, final int sqsize ) {
		final int newsize = image.size( ).x / sqsize * (sqsize + 1);
		final CoordGrid<Boolean> newpixels = new CoordGrid<>( newsize, newsize, false );
		
		// go over the entire grid in windows of the size
		// also keep track of applied patterns
		final int ysteps = image.size( ).y / sqsize;
		final int xsteps = image.size( ).x / sqsize;
		for( int y = 0; y < ysteps; y++ ) {
			for( int x = 0; x < xsteps; x++ ) {
				// extract the square
				final CoordGrid<Boolean> part = image.extract( new Coord2D( x * sqsize, y * sqsize ), new Coord2D( (x + 1) * sqsize - 1, (y + 1) * sqsize - 1 ) );
				
				// find a rule that matches
				final FractalRule rule = rules.stream( ).filter( r -> r.matches( part ) ).findFirst( ).get( );

				// and fill the new image with it
				newpixels.insert( new Coord2D( x * (sqsize + 1), y * (sqsize + 1) ), rule.newpattern.toGrid( ) );
			}
		}

		// fix the image size and swap the images
		newpixels.fixWindow( );
		return newpixels;
	}
	
	/**
	 * Counts the number of pixels that are set after the number of iterations
	 * without actually generating the entire image but instead recursively
	 * splitting the computation in chunks
	 * 
	 * @param rounds The number of iterations to "perform" the fractal art 
	 *   generation process
	 * @return The number of active pixels after generation completed
	 */
	public long countPixels( final int rounds ) {
		final Map<MKey, Long> M = new HashMap<>( );
		return countPixels( M, pixels.copy( ), 0, rounds );
	}

	/**
	 * Counts the number of pixels that will be active in the resulting image by
	 * running an iterative divide and conquer algorithm with memoisation
	 * 
	 * 
	 * @param M The memoisation table
	 * @param part The current part that is being enhanced
	 * @param step The current step in the generation algorithm 
	 * @param rounds The total number of iterations
	 * @return The count of pixels for this part
	 */
	private long countPixels( final Map<MKey, Long> M, final CoordGrid<Boolean> part, final int step, final int rounds ) {
		// no more pixels to generate?
		if( step >= rounds ) {
			return part.count( true );
		}
		
		// first match rule, then check the memoisation table for the matched 
		// pattern as there is only one match pattern that encoded up to 8 
		// different input grids
		final MKey mkey = new MKey( part, rounds - step );
		
		// count the number of pixels from here on
		long pixelcount = 0;		
		if( part.size( ).x < 9 ) {
			// check if we've already seen this one
			if( M.containsKey( mkey ) ) {	return M.get( mkey );	}

			// perform another generation step
			pixelcount = countPixels( M, generate( part, step % 3 == 0 ? 3 : 2 ), step + 1, rounds );
		} else {
			// split computation into 9 3x3 parts
			for( final int y : new int[] {0, 3, 6} ) {
				for( final int x : new int[] {0, 3, 6} ) {
					pixelcount += countPixels( M, part.extract( new Coord2D( x, y ), new Coord2D( x + 2, y + 2 ), true ), step, rounds );
				}
			}
		}		
		
		// memoise the outcome of this iteration
		M.put( mkey, pixelcount );
		return pixelcount;
	}
	
	
	/** @return The pixels of the image */
	public CoordGrid<Boolean> getImage( ) {
		return pixels;
	}
	
	/** @return The current image */
	@Override
	public String toString( ) {
		return pixels.toString( b -> b ? "#" : "." );
	}

	/**
	 * Captures a single fractal generation rule
	 */
	private class FractalRule {
		/** The pattern to match for the rule to fire */
		private final FractalPattern match;
		
		/** The new pattern that is to be used if the rule matches */
		private final FractalPattern newpattern;
		
		/**
		 * Creates a new pattern rule from a string description
		 * 
		 * @param rule The rule as string 
		 */
		private FractalRule( final String rule ) {
			final String[] r = rule.split( " => " );
			this.match = new FractalPattern( r[0] );
			this.newpattern = new FractalPattern( r[1] );
		}
		
		/**
		 * Checks if the match pattern matches the image
		 * 
		 * @param image The image as a grid of pixels
		 * @return True if the rule matches the input image 
		 */
		public boolean matches( final CoordGrid<Boolean> image ) {
			return match.matches( image );
		}
		
		/** @return The rule as a string */
		@Override
		public String toString( ) {
			return match + " => " + newpattern;
		}
	}
	
	/**
	 * Simple memoisation key for fractal pattern + iterations to go 
	 */
	private class MKey extends LabeledObject {
		/**
		 * Creates a new memoisation key
		 * 
		 * @param grid The current grid image
		 * @param steps The number of rounds left 
		 */
		private MKey( final CoordGrid<Boolean> grid, final int steps ) {
			super( "[" + steps + "]" + FractalPattern.fromGrid( grid ).toString( ) );
		}
	}
}
