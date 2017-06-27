package movieproject;

import java.util.*;
import info.movito.themoviedbapi.*;
import info.movito.themoviedbapi.TmdbMovies.MovieMethod;
import info.movito.themoviedbapi.model.*;
import info.movito.themoviedbapi.model.config.*;
import info.movito.themoviedbapi.model.core.*;
import info.movito.themoviedbapi.model.people.*;

public class Test {

	private static TmdbApi tmdbApi;
	private static SessionToken sessionToken;
	
	public static void main(String[] args) {
		tmdbApi = new TmdbApi("1ff803482bfef0b19c8614ac392775e8");
		
		// certain methods in TMDb API require a session id as a parameter, so
		// let's generate it and have it ready
		sessionToken = getSessionToken();
		
		// demo of retrieving information on movies
		demoMovies();
		
		// demo of retrieving information on a specific movie
		demoMovieFeatures();
		
		// demo of searching features of TMDb API
		demoSearchFeatures();
		
		// demo of account related features of TMDb API
		// demoAccountFeatures();
	}

	private static SessionToken getSessionToken() {
		// There are two ways to generate a session id
		
		// Method 1: Generating session id using API calls (requires username and password)
		/*
		TmdbAuthentication tmdbAuth = tmdbApi.getAuthentication();
		TokenSession tokenSession = tmdbAuth.getSessionLogin("username","password");
		System.out.println("Session ID: " + tokenSession.getSessionId());
		SessionToken sessionToken = new SessionToken(tokenSession.getSessionId());
		*/
		
		// Method 2: Generating session id via the website (user interaction involved)
		// Step 1: create a new request token
		//		http://api.themoviedb.org/3/authentication/token/new?api_key=your-api-key
		//		(note down the request_token from the response)
		// Step 2: ask the user for permission
		//		https://www.themoviedb.org/authenticate/request_token
		// Step 3: create a session id
		//		http://api.themoviedb.org/3/authentication/session/new?api_key=api-key&request_token=request-token
		//		(use session-id value in the response to set the value for sessionId variable in the code below
		
		// hard-coded session id generated from Method 1 above
		SessionToken sessionToken = new SessionToken("sessionid generated above");
		return sessionToken;
	}
	
	private static void demoMovies() {
		TmdbMovies tmdbMovies = tmdbApi.getMovies();
		
		// top rated movies
		MovieResultsPage results = tmdbMovies.getTopRatedMovies("en", 0);
		
		System.out.println("=======Top rated movies============");
		Iterator<MovieDb> iterator = results.iterator();
		while (iterator.hasNext()) {
			MovieDb movie = iterator.next();
			System.out.println(movie.getTitle());
			System.out.println(movie.getOriginalTitle());
			System.out.println(movie.getReleaseDate());
			System.out.println();
		}
		System.out.println("====================================");
	}
	
	private static void demoMovieFeatures() {
		TmdbMovies tmdbMovies = tmdbApi.getMovies();
		
		// retrieve information on a movie given movie id
		MovieDb movie = tmdbMovies.getMovie(271110, "en", MovieMethod.credits, MovieMethod.reviews, MovieMethod.videos);
		System.out.println(movie.getTitle());
		System.out.println(movie.getReleaseDate());
		System.out.println(movie.getRuntime());
		System.out.println(movie.getOverview());
		System.out.println();
		
		// retrieve information on movie cast
		List<PersonCast> cast = movie.getCast();
		if (cast == null) {
			System.out.println("Cast info not available for this movie");
		} else {
			Iterator<PersonCast> iterator = cast.iterator();
			while (iterator.hasNext()) {
				PersonCast person = iterator.next();
				System.out.println(person.getName());
				System.out.println(person.getCharacter());
				System.out.println();
			}
		}
		
		// retrieve information on movie crew
		List<PersonCrew> crew = movie.getCrew();
		if (crew == null) {
			System.out.println("Crew info not available for this movie");
		} else {
			Iterator<PersonCrew> iterator = crew.iterator();
			while (iterator.hasNext()) {
				PersonCrew person = iterator.next();
				System.out.println(person.getName());
				System.out.println(person.getJob());
				System.out.println();
			}
		}
		
		// get review for the movie
		List<Reviews> reviews = movie.getReviews();
		if (reviews == null) {
			System.out.println("Reviews not available for this movie");
		} else {
			Iterator<Reviews> iterator = reviews.iterator();
			while (iterator.hasNext()) {
				Reviews review = iterator.next();
				System.out.println(review.getAuthor());
				System.out.println(review.getContent());
				System.out.println();
			}
		}
		
		// retrieve information on movie videos
		List<Video> videos = movie.getVideos();
		if (videos == null) {
			System.out.println("Videos info not available for this movie");
		} else {
			Iterator<Video> iterator = videos.iterator();
			while (iterator.hasNext()) {
				Video video = iterator.next();
				System.out.println(video.getSite());
				System.out.println(video.getKey());
				System.out.println(video.getName());
				System.out.println(video.getType());
				System.out.println(video.getSize());
				System.out.println();
			}
		}
	}
	
	private static void demoSearchFeatures() {
		TmdbSearch tmdbSearch = tmdbApi.getSearch();
		
		// search for movies containing "civil war" in title
		MovieResultsPage results = tmdbSearch.searchMovie("civil war", 0, "en", false, 0);
		Iterator<MovieDb> iterator = results.iterator();
		while (iterator.hasNext()) {
			MovieDb movie = iterator.next();
			System.out.println(movie.getTitle());
			System.out.println(movie.getOriginalTitle());
			System.out.println(movie.getReleaseDate());
			System.out.println();
		}	
	}
	
	private static void demoAccountFeatures() {
		TmdbAccount tmdbAccount = tmdbApi.getAccount();
		
		// get basic account information
		Account act = tmdbAccount.getAccount(sessionToken);
		System.out.println(act.getId());
		System.out.println(act.getUserName());
		System.out.println(act.getName());
		System.out.println();
		
		// get favorite movies of the account
		AccountID actId = new AccountID(act.getId());
		MovieResultsPage results = tmdbAccount.getFavoriteMovies(sessionToken, actId);
		System.out.println("Movies in the favorite list: " + results.getTotalResults());
		Iterator<MovieDb> iterator = results.iterator();
		while (iterator.hasNext()) {
			MovieDb movie = iterator.next();
			System.out.println(movie.getTitle());
			System.out.println(movie.getReleaseDate());
			System.out.println();
		}
		
		// get movies in the watch list of the account
		results = tmdbAccount.getWatchListMovies(sessionToken, actId, 0);
		System.out.println("Movies in the watch list: " + results.getTotalResults());
		iterator = results.iterator();
		while (iterator.hasNext()) {
			MovieDb movie = iterator.next();
			System.out.println(movie.getTitle());
			System.out.println(movie.getId());
			System.out.println(movie.getReleaseDate());
			System.out.println();
		}	
	}
}