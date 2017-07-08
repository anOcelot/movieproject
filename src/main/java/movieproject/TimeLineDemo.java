package movieproject;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import info.movito.themoviedbapi.*;
import info.movito.themoviedbapi.TmdbMovies.MovieMethod;
import info.movito.themoviedbapi.model.*;
import info.movito.themoviedbapi.model.config.*;
import info.movito.themoviedbapi.model.core.*;
import info.movito.themoviedbapi.model.people.*;

public class TimeLineDemo extends Application {

	private static TmdbApi tmdbApi;
	private static SessionToken sessionToken;
	
	@Override
    public void start(Stage primaryStage) {
        
		Button btn = new Button();
        btn.setText("Let the tendies hit the floor");
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            //@Override
            public void handle(ActionEvent event) {
                System.out.println(sessionToken.toString());
            }
        });
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        scrollPane.setContent(root);
        
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

 Scene scene = new Scene(scrollPane, 600, 700);

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
