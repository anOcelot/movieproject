package movieproject;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbPeople;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.TmdbPeople.PersonResultsPage;
import info.movito.themoviedbapi.model.core.SessionToken;
import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.people.PersonCredit;
import info.movito.themoviedbapi.model.people.PersonCredits;
import junit.framework.Assert;
import junit.framework.TestCase;

public class ActorTimeLineTest extends TestCase {
	
	private TmdbApi tmdbApi;
	private TmdbSearch search;
	private TmdbMovies movies;
	private TmdbPeople people;
	private PersonResultsPage results;
	
	

	@Test 
	public void testResources() {
		
		tmdbApi = new TmdbApi("1ff803482bfef0b19c8614ac392775e8");
		SessionToken sessionToken = TimeLineDemo.getSessionToken();
		
	
		
		search = tmdbApi.getSearch();
		people = tmdbApi.getPeople();
		movies = tmdbApi.getMovies();	
		results = search.searchPerson("Adam Sandler", true, 0);
		

		
	}
	
	@Test
	public void testConstructor() {
		
		testResources();
	
		
		/** Iterator to access results */
		Iterator<Person> iterator = results.iterator();
		
		
		while (iterator.hasNext()){
		Person actor = iterator.next();
		PersonCredits credits = people.getPersonCredits(actor.getId());
		
		ActorTimeLine career = new ActorTimeLine(credits, movies);
		
		for (PersonCredit c: career.getCast()) {
			career.getRevenue(c);
		}
		}
		}

		
	@Test 
	public void testComparator(){
		
		testResources();
		Iterator<Person> iterator = results.iterator();
		Person actor = iterator.next();
		PersonCredits credits = people.getPersonCredits(actor.getId());
		ActorTimeLine career = new ActorTimeLine(credits, movies);
		
		List<PersonCredit> c = career.getCast();
		
			
		Assert.assertEquals(career.compare(c.get(0), c.get(10)), -1);
		Assert.assertEquals(career.compare(c.get(10), c.get(5)), 1);
		Assert.assertEquals(career.compare(c.get(15), c.get(15)), 0);
		
	}
	
	@Test
	public void testGui() {
		
		TimeLineDemo.main(null);
	}

}
