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
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
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
	
	/** Another VBox to use with other features later. */
	@FXML private VBox contentPane2;
	
	TmdbApi tmdbApi;
	SessionToken sessionToken;

	/**
	 * when this method is called, JavaFX instantiates 
	 * the @FXML classes in the background according to the layout.
	 */
	public void initialize() {
		
		tmdbApi = new TmdbApi("1ff803482bfef0b19c8614ac392775e8");
		sessionToken = getSessionToken();
		
		tmdbSearch = tmdbApi.getSearch();
		
		tmdbPeople = tmdbApi.getPeople();
		
		movies = tmdbApi.getMovies();
		
		
		searchButton.setOnAction(new SearchHandler());
		
		resultsPane.setPadding(new Insets(20));
		resultsPane.setSpacing(10);

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

		/** clear the pane **/
		resultsPane.getChildren().clear();
		
		/** first, find the people. */
		PersonResultsPage results = tmdbSearch
				.searchPerson(str, true, 0);
		
		/** Iterator to access results */
		Iterator<Person> iterator = results.iterator();

		NumberFormat formatter = NumberFormat.getCurrencyInstance();

		
		


		while(iterator.hasNext()) {
			
			Person actor = iterator.next();
			
			/** get the person's name */
			Label nameLabel = new Label(actor.getName());
			

			/** get the person's film credits */
			PersonCredits credits = tmdbPeople
					.getPersonCredits(actor.getId());
		
			ActorTimeLine career = new ActorTimeLine(credits, movies);
			resultsPane.getChildren().add(nameLabel);
			for (PersonCredit c: career.getCast()){
				
				Image cover = new Image ("https://image.tmdb.org/t/p/original/" + c.getPosterPath(), 
						240, 
						360, 
						false, 
						false);
				String next = c.getMovieTitle() + " " + c.getReleaseDate() + " ";
				next += formatter.format(career.getRevenue(c));
				
				resultsPane.getChildren().addAll(new Label(next), new ImageView(cover));
			}

			
			
			
			// add movies without known release dates at beginning of list
			
			// add rest of movies
			for (PersonCredit c:datedCredits){
				Image cover = new Image("https://image.tmdb.org/t/p/original/" + c.getPosterPath(),
						240, 
						360, 
						false, 
						false);
				
				resultsPane.getChildren().addAll(new Label(c.getMovieTitle() + " - " + c.getReleaseDate() + "\n\n"),
						new ImageView(cover));
			}
		}
	}
			
	

		
	private class SearchHandler implements EventHandler {

		public void handle(Event event) {
			demoSearchFeatures(searchBox.getText());
			
		}
		
	}

	/**
	 * Main method launches the Application.
	 * @param args
	 */
	public static void main(String[] args) {
		
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

	

	
