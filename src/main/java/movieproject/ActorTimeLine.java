package movieproject;

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

public class ActorTimeLine implements Comparator<PersonCredit> {
	
	
	private TmdbMovies movies;
	
	private PersonCredits credits;
	private ArrayList<PersonCredit> cast;
	
	
	public ActorTimeLine(PersonCredits credits, TmdbMovies movies) {
		
		this.movies = movies;
		this.credits = credits;
		
		
		
	}
	

	
	public List<PersonCredit> getCast(){
		
		List<PersonCredit> cast = credits.getCast();
		cast.sort(this);
		return cast;
	}
	
	public long getRevenue(PersonCredit c) {
		long revenue = movies.getMovie(c.getId(), "english").getRevenue();
		return revenue;
	}
	
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
