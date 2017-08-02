package movieproject;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbPeople;
import info.movito.themoviedbapi.TmdbPeople.PersonResultsPage;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.Artwork;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
/**
 * Main class instantiates a gui which allows the users enter an actor's
 * name, then uses this user input to generate an ActorTimeLine for that actor
 * and present a visualization.
 * @author Pieter Holleman, Zachary Hern, Adam Slifco
 * @version 2
 * @since 7-7-2017
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
	@FXML private Pane contentPane;
	
	/** Another VBox to use with other features later. */
	@FXML private VBox contentPane2;
	
	private Person actor;
	
	
	
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
	 * @version 1
	 * @since 7-7-2017
	 */
	
	@Override
	public void start(final Stage stage) throws Exception {
	
		/** loader object interacts with FXML layout */
		FXMLLoader loader = new FXMLLoader(getClass()
				.getResource("/Sample.fxml"));

	    /** loaders gonna load */
		Scene scene = new Scene((Parent) loader.load());

		stage.setTitle("Actor Timeline");
		stage.setScene(scene);
		stage.show();
		
		datedCredits = new LinkedList<PersonCredit>();
		
		datelessCredits = new LinkedList<PersonCredit>();
		
	
	}
	
	/**
	 * Basic Search Method
	 * @param str The name of the actor to search for
	 */
	private void search(final String str) {
		
		/** clear the pane **/
		resultsPane.getChildren().clear();
		
		/** first, find the people. */
		PersonResultsPage results = tmdbSearch
				.searchPerson(str, true, 0);
		
		/** Iterator to access results */
		Iterator<Person> iterator = results.iterator();
		
		
			
			actor = iterator.next();
			
			/** get the person's name */
			Label nameLabel = new Label(actor.getName());
			
			TextFlow flow = new TextFlow(new Hyperlink(actor.getName()));
			
			/** get the person's film credits */
			PersonCredits credits = tmdbPeople
					.getPersonCredits(actor.getId());
			
		
			 
			
			List<Artwork> images = tmdbPeople.getPersonImages(actor.getId());
			String picPath = images.get(0).getFilePath();
			Image pic = new Image ("https://image.tmdb.org/t/p/original/" + picPath, 
					240, 
					360, 
					false, 
					false);
			ActorTimeLine career = new ActorTimeLine(credits, movies);
			
			ImageView picView = new ImageView(pic);
			FXMLLoader loader = new FXMLLoader(this.getClass()
					.getResource("/SummaryBox.fxml"));
			
			//drawTimeline(career);
			
		    VBox summaryBox = new SummaryBox();
			
			
		    
		  
			
			try {
				summaryBox = loader.load();
				summaryBox.getChildren().add(flow);
				summaryBox.getChildren().add(picView);
				
				resultsPane.getChildren().add(summaryBox);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			displayCredits(actor);
			
		
	}
	
//	private class DisplayHandler implements EventHandler {
//		
//		private void handle(Event event){
//			displayCredits(actor);
//		}
//	}
	private void displayCredits(Person actor) {
		
		PersonCredits credits = tmdbPeople
				.getPersonCredits(actor.getId());
		
	
		/** clear the pane **/
		resultsPane.getChildren().clear();
		
		

		final NumberFormat formatter = NumberFormat.getCurrencyInstance();

		
		final ActorTimeLine career = new ActorTimeLine(credits, movies);
		
//		FXMLLoader loader = new FXMLLoader(this.getClass()
//				.getResource("/SummaryBox.fxml"));
//	
		
		
		
	
			
		

		
		
		
		
			/******************************stolecode for testing*********************/
			//final ProgressIndicator pi = new ProgressIndicator(0);
            //root.getChildren().add(pi);
        	

            // separate non-FX thread
            new Thread() {
                // runnable for that thread
                public void run() {
                	for (final PersonCredit c: career.getCast()){
                		final SummaryBoxV2 box = new SummaryBoxV2();
        			Image cover = new Image ("https://image.tmdb.org/t/p/original/" + c.getPosterPath(), 
        					240, 
        					360, 
        					false, 
        					false);
        			String next = c.getMovieTitle() + " " + c.getReleaseDate() + " ";
        			String revenue = formatter.format(career.getRevenue(c));
        			Label titleLabel = new Label(next);
        			Label revenueLabel = new Label(revenue);
        			box.add(titleLabel);
        			box.add(new ImageView(cover));
        			box.add(revenueLabel);
                    //for (int i = 0; i < 20; i++) {
                       // try {
                            // imitating work
                         //   Thread.sleep(new Random().nextInt(1000));
                        //} catch (InterruptedException ex) {
                          //  ex.printStackTrace();
                        //}
                       // final double progress = i*0.05;
                        // update ProgressIndicator on FX thread
                        Platform.runLater(new Runnable() {

                            public void run() {
                            	resultsPane.getChildren().addAll(box.getBox());
                            }
                        });
                    }
                }
            }.start();
			/******************************stolecode for testing*********************/
			

			
		
		
	}
	

	/**
	 * Searches for an actor and displays their film credits,
	 * with posters and revenue info, to the gui.
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
			drawTimeline(career);
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

		}
	}
			
	

	/**
	 * 	Inner class for handling search button function
	 * @author pieter holleman, Zachary Hern, Adam Slifco
	 */
	private class SearchHandler implements EventHandler {

		public void handle(Event event) {
			//demoSearchFeatures(searchBox.getText());
			search(searchBox.getText());
			
			resultsPane.getChildren().add(new SummaryBox());
		}
		
	}
	
	

	/**
	 * Main method launches the Application.
	 * @param args 
	 */
	public static void main(String[] args) {
		
		launch(args);

	}
	
	
	
	 public void drawTimeline(ActorTimeLine career){
	    	
	    	List<PersonCredit> roles = career.getCast();
	    	int size = 0;
	    	String date;
	    	int i = 0;
	    	
	    	while (size == 0){
	    		
	    	try {
	    		date = "2017";
	    		size = Integer.parseInt(date);
	    		date = roles.get(0).getReleaseDate().split("-")[0];
	    		size -= Integer.parseInt(date);
	    		size *= 100;
	    		
	    	} catch (Exception E) {
	    		++i;
	    	}
	    	}
	    	
	    	 final Rectangle rectBasicTimeline = new Rectangle(0, 10, 0, 10);
			 final Text text = new Text("test");
		     rectBasicTimeline.setFill(Color.FORESTGREEN);
		     final Timeline timeline = new Timeline();
		     //timeline.setCycleCount(Timeline.INDEFINITE);
		     //timeline.setAutoReverse(true);
		     final KeyValue kv = new KeyValue(rectBasicTimeline.widthProperty(), size);
		     final KeyFrame kf = new KeyFrame(Duration.millis(size), kv);
		     timeline.getKeyFrames().add(kf);
		     
		     
		     AnimationTimer timer;
		     timer = new AnimationTimer() {
		            @Override
		            public void handle(long l) {
		                text.setText(Double.toString(rectBasicTimeline.getWidth()));
		                
		            }
		        };
		        
		     rectBasicTimeline.relocate(35, 400);
		     text.relocate(35, 375);
		     
		     contentPane.getChildren().clear();
		     contentPane.getChildren().add(text);
		     contentPane.getChildren().add(rectBasicTimeline);
		    
		     
		     

		      
		 
		     timeline.play();
		     timer.start();
		     
	    }

	/**
	 * Gets a session token.
	 * @return - the session token
	 */
	static SessionToken getSessionToken() {
		
	SessionToken sessionToken = new SessionToken("sessionid generated above");
	
		return sessionToken;
	}
	
	}

	

	
