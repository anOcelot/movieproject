package movieproject;
/**
 * Package to hold the project
 */

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.people.PersonCredit;
import info.movito.themoviedbapi.model.people.PersonCredits;



/********************************************************************
 * ActorTimeLine is essentially a wrapper for the PersonCredits class
 * that uses a TmdbMovies object to all the retrieval of additional 
 * categories of info, such as the revenue generated by a referenced
 * by a film in an actor's
 * PersonCredits.
 * @author Pieter Holleman, Zachary Hern, Adam Slifco
 * @version 2
 * @since 7-7-2017
 *********************************************************************/
public class ActorTimeLine implements Comparator<PersonCredit> {
	
	/**
	 * TmdbMovies object allows accessing more info about a movie credit
	 * than the current implementation of PersonCredit Allows.
	 */
	private TmdbMovies movies;
	
	/** the Tmdb database-sourced PersonCredits object. */
	private PersonCredits credits;
	
	/********************************************************************
	 * Constructor uses credits and tmdb movies object to create a 
	 * PersonCredits wrapper that uses the movies object to allow
	 * accessing additional info.
	 * @param credits - any given actors credits
	 * @param movies - the TmdbMovies object used by the class
	 *********************************************************************/
	public ActorTimeLine(final PersonCredits credits,
			final TmdbMovies movies) {
		
		this.movies = movies;
		this.credits = credits;
	
	}
	

	/********************************************************************
	 * essentially overrides the getCast() method in PersonCredits,
	 * allowing it to be sorted.
	 * @return - a sorted list of PersonCredits 
	 ********************************************************************/
	public List<PersonCredit> getCast() {
		
		List<PersonCredit> cast = credits.getCast();
		cast.sort(this);
		return cast;
	}

	
	/********************************************************************
	 * uses the TmdbMovies object to get the revenue of the film in
	 * which an actor was credited.
	 * @param c - the film credit
	 * @return the revenue generated by the film referenced in the credit. 
	 *********************************************************************/
	public long getRevenue(final PersonCredit c) {
		long revenue = movies.getMovie(c.getId(),
				"english").getRevenue();
		return revenue;
	}
	
	/********************************************************************
	 * (non-Javadoc).
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 * @param a - first PersonCredit
	 * @param b - second PersonCredit
	 * @return int
	 ********************************************************************/
	public int compare(final PersonCredit a, final PersonCredit b) {
		
		
		if (a.getReleaseDate() == null) {
			return -1;
		} else if (b.getReleaseDate() == null) {
			return -1;
		}
		
		String[] releaseDateA = a.getReleaseDate().split("-");
		String[] releaseDateB = b.getReleaseDate().split("-");
		
		LocalDate one = LocalDate.of(Integer.parseInt(releaseDateA[0]), 
				Integer.parseInt(releaseDateA[1]), 
				Integer.parseInt(releaseDateA[2]));
		
		LocalDate two = LocalDate.of(Integer.parseInt(releaseDateB[0]), 
				Integer.parseInt(releaseDateB[1]), 
				Integer.parseInt(releaseDateB[2]));
		
		if (one.isAfter(two)) { 
			return 1; 
			} else if (one.isEqual(two)) {
				return 0;
			} else {
				return -1;
			}
		
		
	}
	
}
