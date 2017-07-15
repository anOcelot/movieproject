package movieproject;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

import info.movito.themoviedbapi.*;
import info.movito.themoviedbapi.TmdbMovies.MovieMethod;
import info.movito.themoviedbapi.model.*;
import info.movito.themoviedbapi.model.config.*;
import info.movito.themoviedbapi.model.core.*;
import info.movito.themoviedbapi.model.people.*;

public class GraphTest extends Application {

	private static TmdbApi tmdbApi;
	private static SessionToken sessionToken;
	Label message = new Label("");
   
	
	
    	@Override
	    public void start(Stage primaryStage) {
	        
			Button btn = new Button();
			btn.setText("I'm a button plz click");
	        
	        ScrollPane scrollPane = new ScrollPane();
	        scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
	        
	        Pane root = new Pane();
	        root.setPrefSize(600, 700);
	        scrollPane.setContent(root);
	        
	        
	       
	        
	       
	        
	        btn.setOnAction(new EventHandler<ActionEvent>() {
	 
	            //@Override
	            public void handle(ActionEvent event) {
	                System.out.println(tmdbApi.toString());
	               
	            }
	        });
	       
	        
	       Circle circles[] = new Circle[10];
	       Random rnd = new Random();
	     
	       
	       for (int i = 0; i < 10; ++i){
	    	   
	    	circles[i] = new Circle(10, Color.BLUE);
	    	
	    	circles[i].relocate(rnd.nextInt(600), rnd.nextInt(500));
	    	root.getChildren().add(circles[i]);
	    	
	    	
	       }
	        
	        scrollPane.setContent(root);
	        scrollPane.setFitToHeight(true);
	        scrollPane.setFitToWidth(true);

	 Scene scene = new Scene(scrollPane, 700, 600);
	 		
	 
	
	        primaryStage.setTitle("Hello World!");
	        primaryStage.setScene(scene);
	        primaryStage.show();
	        
	       
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
	
	

