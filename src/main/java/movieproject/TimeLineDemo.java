package movieproject;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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

public class TimeLineDemo extends Application {

	private static TmdbApi tmdbApi;
	private static SessionToken sessionToken;
	private static VBox resultsPane = new VBox();
	private BorderPane root = new BorderPane();
	TextField searchBox = new TextField("Enter the name of an Actor");
    
	@Override
	
	
	
    public void start(Stage primaryStage) {
        
		Button btn = new Button();
		btn.setText("Search");
		//TextField searchBox = new TextField("Enter the name of an Actor");
		searchBox.setPrefWidth(400);
		
        //placeholders for other features
        Pane left = new Pane();
        Pane right = new Pane();
        Pane top = new Pane();

        
        root.setStyle("-fx-background-color:forestgreen");
        left.setStyle("-fx-background-color:darkgreen");
        top.setStyle("-fx-background-color:seagreen");
        right.setStyle("-fx-background-color:mediumseagreen");
        resultsPane.setStyle("-fx-background-color:coral");
        
       
       
        
        FlowPane searchPane = new FlowPane();
        searchPane.setPrefWidth(200);
        searchPane.setHgap(10);
        searchPane.getChildren().addAll(searchBox, btn);
        
        root.setCenter(searchPane);
        root.setMargin(searchPane, new Insets(25,25, 25, 25));
        
        root.setLeft(left);
        root.setRight(right);
        root.setTop(top);
        root.setBottom(resultsPane);
       
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
       
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            //@Override
            public void handle(ActionEvent event) {
               demoSearchFeatures(searchBox.getText());
               resultsPane.getChildren().add(new Label("Let the tendies hit the floor"));
            }
        });
       
        //Drop the borderpane into the stack pane
        
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        
        Scene scene = new Scene(scrollPane, 1200, 600);
        
        double sideWidth = scene.getWidth()/3;
        double height = scene.getHeight()/3;
        left.setPrefSize(sideWidth, height);
        right.setPrefSize(sideWidth, height);
 		top.setPrefSize(scene.getWidth(), height);
 		searchPane.setPrefSize(sideWidth, height);
 		resultsPane.setPrefSize(scene.getWidth(), height);
 		
 		
 		searchPane.setPrefWidth(scene.getWidth()/2);
 		searchPane.setPrefHeight(scene.getHeight()/3);
 		
 	
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
        
       
    }
	

	
	private static void demoSearchFeatures(String str) {
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
