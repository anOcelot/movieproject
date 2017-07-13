package movieproject;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Iterator;

import info.movito.themoviedbapi.*;
import info.movito.themoviedbapi.TmdbMovies.MovieMethod;
import info.movito.themoviedbapi.TmdbPeople.PersonResultsPage;
import info.movito.themoviedbapi.model.*;
import info.movito.themoviedbapi.model.config.*;
import info.movito.themoviedbapi.model.core.*;
import info.movito.themoviedbapi.model.people.*;
import javafx.fxml.FXML;

public class TimeLineDemo extends Application {

	//static API and sessionToken
	private static TmdbApi tmdbApi;
	private static SessionToken sessionToken;
	
	//the @FXML means the FXML layout contains instructions to inject these classes, 
	//so they don't need to be instantiated. 
	@FXML private VBox resultsPane;
	@FXML private Button searchButton;
	@FXML private TextField searchBox;
	@FXML private VBox contentPane;
	@FXML private VBox contentPane2;
	
    
	
	//when this method is called, JavaFX instantiates the @FXML classes
	//in the background according to the layout. 
	public void initialize(){
		
		 //assign button actions in here. searchButton can be accessed even though
		 //there is no code here instantiating it.
		 
		searchButton.setOnAction(new EventHandler<ActionEvent>() {
			 
			  public void handle(ActionEvent event) {
	               demoSearchFeatures(searchBox.getText());
	               resultsPane.getChildren().add(new Label("Let the tendies hit the floor"));
	            } 
			 
		 });
		 
		contentPane.getChildren().add(new Label("Stuff can go here."));
		contentPane2.getChildren().add(new Label("Stuff can go here too."));

		 

		
	}
	
 
	
    @Override
	public void start(Stage stage) throws Exception{
		
    	 //loader object interacts with fxml layout. Use "gluon scene builder" plugin 
    	 //if you wanna mess with the fxml layout in (resources/Sample.fxml)
		 FXMLLoader loader = new FXMLLoader(getClass().getResource("/Sample.fxml"));
		 
		 //loaders gonna load
		
		 Scene scene = new Scene((Parent) loader.load());
		 
		
		 stage.setTitle("Scene title");
		 stage.setScene(scene);
		 stage.show();
	}
	

	
	private void demoSearchFeatures(String str) {
		
		TmdbSearch tmdbSearch = tmdbApi.getSearch();
		TmdbPeople tmdbPeople = tmdbApi.getPeople();
		// search for movies containing "civil war" in title
		PersonResultsPage results = tmdbSearch.searchPerson(str, true, 0);
		Iterator<Person> iterator = results.iterator();
		
		while (iterator.hasNext()) {
			
			Person person = iterator.next();
			PersonCredits credits = tmdbPeople.getPersonCredits(person.getId());
			Label nameLabel = new Label(person.getName());
			
			resultsPane.getChildren().addAll(nameLabel);
			
			for (PersonCredit c:credits.getCast()){
				resultsPane.getChildren().add(new Label(c.getMovieTitle()));
			}
			
			
			
			
			
			}
	}	
	
	
	
 public static void main(String[] args) {
        
	 tmdbApi = new TmdbApi("1ff803482bfef0b19c8614ac392775e8");
		
		// certain methods in TMDb API require a session id as a parameter, so
		// let's generate it and have it ready
		sessionToken = getSessionToken();
	
	 	launch(args);
       
	  
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
	
}
