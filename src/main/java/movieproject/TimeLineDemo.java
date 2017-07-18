package movieproject;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbPeople;
import info.movito.themoviedbapi.TmdbPeople.PersonResultsPage;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.core.SessionToken;
import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.people.PersonCredit;
import info.movito.themoviedbapi.model.people.PersonCredits;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
 * 
 * @author Pieter Holleman
 *
 */
public class TimeLineDemo extends Application {


	/** static API. */
	private static TmdbApi tmdbApi;
	
	/** static sessionToken. */
	private static SessionToken sessionToken;
	
	private TmdbMovies movies;
	

	/** might run faster if these are here. */
	private TmdbSearch tmdbSearch; 

	/** might run faster if these are here. */
	private TmdbPeople tmdbPeople; 

	/** list of movie credits with release dates. */
	private LinkedList<PersonCredit> datedCredits;
	
	/** list of movie credits without release dates. */
	private LinkedList<PersonCredit> datelessCredits;
	
	/** Pane to store results. */
	@FXML private VBox resultsPane;
	
	/**  search button. */
	@FXML private Button searchButton;
	
	/** search field.*/
	@FXML private TextField searchBox;
	
	/** VBox to list search results (movies or actors). */
	@FXML private VBox contentPane;
	
	/** Another Vbox to use with other features later. */
	@FXML private VBox contentPane2;

	/**
	 * when this method is called, JavaFX instantiates 
	 * the @FXML classes in the background according to the layout.
	 */
	public void initialize() {
		
		
		tmdbSearch = tmdbApi.getSearch();
		
		tmdbPeople = tmdbApi.getPeople();
		
		movies = tmdbApi.getMovies();
		
		
		searchButton.setOnAction(new SearchHandler());

		contentPane.getChildren()
			.add(new Label("Stuff can go here."));
		contentPane2.getChildren()
			.add(new Label("Stuff can go here too."));

	}
	
	/**
	 * Inner EventHandler class for search button.
	 * @author Pieter Holleman
	 *
	 */
	
	@Override
	public void start(final Stage stage) throws Exception {
	
		/** loader object interacts with FXML layout */
		FXMLLoader loader = new FXMLLoader(getClass()
				.getResource("/Sample.fxml"));

	    /** loaders gonna load */
		Scene scene = new Scene((Parent) loader.load());

		stage.setTitle("Scene title");
		stage.setScene(scene);
		stage.show();
		
		datedCredits = new LinkedList<PersonCredit>();
		
		datelessCredits = new LinkedList<PersonCredit>();
		
	
	}

	/**
	 * currently testing this feature.
	 * @param str - passed to search method
	 */
	private void demoSearchFeatures(final String str) {

		/** first, find the people. */
		PersonResultsPage results = tmdbSearch
				.searchPerson(str, true, 0);
		
		/** Iterator to access results */
		Iterator<Person> iterator = results.iterator();
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		while (iterator.hasNext()) {
			
			Person actor = iterator.next();
			
			Label nameLabel = new Label(actor.getName());
		
			/** get the person's film credits */
			PersonCredits credits = tmdbPeople
					.getPersonCredits(actor.getId());
		
			ActorTimeLine career = new ActorTimeLine(credits, movies);
			resultsPane.getChildren().add(nameLabel);
			for (PersonCredit c: career.getCast()){
				
				Image cover = new Image ("https://image.tmdb.org/t/p/original/" + c.getPosterPath(), 540, 800, false, false);
				String next = c.getMovieTitle() + " " + c.getReleaseDate() + " $";
				next += formatter.format(career.getRevenue(c));
				
				resultsPane.getChildren().addAll(new Label(next), new ImageView(cover));
			}
			}
						
		}
			
//			datedCredits = new LinkedList<PersonCredit>();
//			
//			datelessCredits = new LinkedList<PersonCredit>();
//			
//			dateComparator = new DateComparator();
//
//			Person person = iterator.next();
//
//			/** get the person's name */
//			Label nameLabel = new Label(person.getName());
//			
//			/** get the person's film credits */
//			PersonCredits credits = tmdbPeople
//					.getPersonCredits(person.getId());
//			
//			/** print name to results */
//			resultsPane.getChildren().addAll(nameLabel);
//			
//
//			/** loop across person's credits and print them */
//
//			
////			for (PersonCredit c:credits.getCast()) {
////				Image cover = new Image ("https://image.tmdb.org/t/p/original/" + c.getPosterPath(), 240, 360, false, false);
////				resultsPane.getChildren()
////					.addAll(new ImageView(cover));
//
//			/** loop across person's credits and adds them to lists*/
//
//			for (PersonCredit c:credits.getCast()){
//				// adds credit to dated credit list if it has one
//				if (c.getReleaseDate() != null) {
//					datedCredits.add(c);
//				}
//				// adds credit to undated credit list if returns null
//				else {
//					datelessCredits.add(c);
//				}

		
			
//			// sorts credits with release date by date
//			Collections.sort(datedCredits, dateComparator);
//			
//			// add movies without known release dates at beginning of list
//			for (PersonCredit c:datelessCredits){
//				Image cover = new Image ("https://image.tmdb.org/t/p/original/" + c.getPosterPath(), 540, 800, false, false);
//				resultsPane.getChildren().addAll(new ImageView(cover), new Label(c.getMovieTitle()), new Label("Release Date Unknown\n\n"));
//			}
//			
//			// add rest of movies
//			for (PersonCredit c:datedCredits){
//				Image cover = new Image ("https://image.tmdb.org/t/p/original/" + c.getPosterPath(), 540, 800, false, false);
//				resultsPane.getChildren().addAll(new ImageView(cover), new Label(c.getMovieTitle()), new Label(c.getReleaseDate() + "\n\n"));
//			}
		
	
	
	
	/**
	 * A private inner EventHandler for the search button.
	 * @author Pieter Holleman
	 *
	 */
	private class SearchHandler implements EventHandler<ActionEvent> {

		/**
		 * Event handler to trigger search.
		 * @param event triggers search
		 */
		public void handle(final ActionEvent event) {
		
			demoSearchFeatures(searchBox.getText());
			
			
		}
		
	};

	/**
	 * Main method launches the Application.
	 * @param args
	 */
	public static void main(String[] args) {

		tmdbApi = new TmdbApi("1ff803482bfef0b19c8614ac392775e8");
		sessionToken = getSessionToken();
		
		launch(args);

	}

	/**
	 * Gets a session token.
	 * @return - the session token
	 */
	private static SessionToken getSessionToken() {
		
	SessionToken sessionToken = new SessionToken("sessionid generated above");
	
		return sessionToken;
	}
	
	
}

