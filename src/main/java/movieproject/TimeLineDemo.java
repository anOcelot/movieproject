package movieproject;

import java.util.Iterator;

import info.movito.themoviedbapi.TmdbApi;
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


	/** might run faster if these are here. */
	private TmdbSearch tmdbSearch; 
	/** might run faster if these are here. */
	private TmdbPeople tmdbPeople; 

	
	
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
		
		TmdbApi tmdbApi = new TmdbApi("1ff803482bfef0b19c8614ac392775e8");
		
		SessionToken sessionToken = getSessionToken();
		
		tmdbSearch = tmdbApi.getSearch();
		
		tmdbPeople = tmdbApi.getPeople();
		
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

		while (iterator.hasNext()) {

		
			Person person = iterator.next();

			/** get the person's name */
			Label nameLabel = new Label(person.getName());
			
			/** get the person's film credits */
			PersonCredits credits = tmdbPeople
					.getPersonCredits(person.getId());
			
			/** print name to results */
			resultsPane.getChildren().addAll(nameLabel);
			
			/** loop across person's credits and print them */

			
			for (PersonCredit c:credits.getCast()) {
				Image cover = new Image ("https://image.tmdb.org/t/p/original/" + c.getPosterPath(), 240, 360, false, false);
				resultsPane.getChildren()
					.addAll(new ImageView(cover));
				
				
			}

		}
	}
	
	
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
		
	}

	/**
	 * Main method launches the Application.
	 * @param args
	 */
	public static void main(String[] args) {

		TmdbApi tmdbApi = new TmdbApi("1ff803482bfef0b19c8614ac392775e8");
		SessionToken sessionToken = getSessionToken();
		
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

