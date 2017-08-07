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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

	/** Pane to store results. */
	@FXML private VBox resultsPane;
	
	/**  search button. */
	@FXML private Button searchButton;
	
	/** search field.*/
	@FXML private TextField searchBox;
	
	/** VBox to list search results (movies or actors). */
	@FXML private VBox creditsPane;
	
	/** Another VBox to use with other features later. */
	@FXML private VBox chartPane;
	
	/** Currently selected actor **/
	private Person actor;
	
	
	TmdbApi tmdbApi;
	SessionToken sessionToken;

	/**
	 * when this method is called, JavaFX instantiates 
	 * the @FXML classes in the background according to the layout.
	 */
	public void initialize() {
		
		/** instantiate all the necessary api resources **/
		tmdbApi = new TmdbApi("1ff803482bfef0b19c8614ac392775e8");
		sessionToken = getSessionToken();
		tmdbSearch = tmdbApi.getSearch();
		tmdbPeople = tmdbApi.getPeople();
		movies = tmdbApi.getMovies();
		
		
		searchButton.setOnAction(new SearchHandler());
		
		resultsPane.setPadding(new Insets(20));
		resultsPane.setSpacing(10);
		
		creditsPane.setPadding(new Insets(20));
		creditsPane.setSpacing(10);
		
		chartPane.setPadding(new Insets(20));
		chartPane.setSpacing(10);
		
		
	}
	
	/**
	 * JavaFX application class requires override of start
	 * method to load FXML document and render the GUI
	 * @author Pieter Holleman
	 * @version 1
	 * @since 7-7-2017
	 */
	
	@Override
	public void start(final Stage stage) throws Exception {
	
		/** loader object interacts with FXML layout */
		FXMLLoader loader = new FXMLLoader(getClass()
				.getResource("/gui.fxml"));

	    /** load the scene **/
		Scene scene = new Scene((Parent) loader.load());

		stage.setTitle("Actor Timeline");
		stage.setScene(scene);
		stage.show();
		
	
	
	}
	
	/**
	 * Basic search method searches for an actor by name starts a thread rendering the results.
	 * @param str The name of the actor to search for
	 */
	private void search(final String str) {
		
		drawChart();
		
		/** clear any previous results pane **/
		resultsPane.getChildren().clear();
		
		/** first, find the people. */
		PersonResultsPage results = tmdbSearch
				.searchPerson(str, true, 0);
		
		/** Iterator to access results */
		Iterator<Person> iterator = results.iterator();
			
		
		Person currentActor = iterator.next();
		
		resultsPane.getChildren().addAll(drawActorBox(currentActor));
		
	
		//CreditTask t = new CreditTask();
		//t.start();
	
		
	}
	

	private class CreditTask implements Runnable {
		
		Thread thread;
		
		
		public void drawCredits(){
			
			PersonCredits credits = tmdbPeople
					.getPersonCredits(actor.getId());
			
			for (PersonCredit c: credits.getCast()){
				
			final VBox newBox = drawFilmBox(c);
			
			Platform.runLater(new Runnable() {

	            public void run() {
	            	chartPane.getChildren().addAll(newBox);
	            }
	        });
			}
			
		}
		
		
		public void run(){
			
			try{
				drawCredits();
				System.out.println("Thread succeeded");
				
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
		public void start(){
				
			if (thread == null){
				thread = new Thread(this, "credit search");
				thread.start();
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
			
			//resultsPane.getChildren().add(new SummaryBox());
		}
		
	}
	
	private class CreditsHandler implements EventHandler {
		
		Person p;
		public CreditsHandler(Person person){
			super();
			p = person;
		}
		
		public void handle(Event event){
			chartPane.getChildren().clear();
			actor = p;
			CreditTask creditThread = new CreditTask();
			creditThread.start();
			//drawChart();
		}
		
		
	}
	
	

	/**
	 * Main method launches the Application.
	 * @param args 
	 */
	public static void main(String[] args) {
		
		launch(args);

	}
	
	
	

	private VBox drawFilmBox(PersonCredit credit){
		
		FXMLLoader loader = new FXMLLoader(this.getClass()
				.getResource("/resultBox.fxml"));
		//loader.setRoot(this);
		VBox newBox = new VBox();
		
		try {
			newBox = loader.load();
			
		} catch (IOException e) {
//		throw new RuntimeException(e);
			e.printStackTrace();
		}
		
		final NumberFormat formatter = NumberFormat.getCurrencyInstance();
		long revenue = movies.getMovie(credit.getId(), "english").getRevenue();
		Image cover = new Image ("https://image.tmdb.org/t/p/original/" + credit.getPosterPath(), 
				240, 
				360, 
				false, 
				false);
		
		String title = credit.getMovieTitle() + " " + credit.getReleaseDate() + " ";
		String revenueStr = formatter.format(revenue);
		
		Label titleLabel = new Label(title);
		Label revenueLabel = new Label(revenueStr);
		
		newBox.getChildren().addAll(titleLabel);
		newBox.getChildren().addAll(new ImageView(cover));
		newBox.getChildren().addAll(revenueLabel);
		
		return newBox;
		
	}
	
	private VBox drawActorBox(Person person){
		
		FXMLLoader loader = new FXMLLoader(this.getClass()
				.getResource("/resultBox.fxml"));
		
		VBox newBox = new VBox();
		Button creditsButton = new Button("Get credits");
		
		creditsButton.setOnAction(new CreditsHandler(person));
		
		try {
			newBox = loader.load();
			
		} catch (IOException e) {
//		throw new RuntimeException(e);
			e.printStackTrace();
		}
		
		
		Label nameLabel = new Label(person.getName());
		
		List<Artwork> images = tmdbPeople.getPersonImages(person.getId());
		String picPath = images.get(0).getFilePath();
		Image pic = new Image ("https://image.tmdb.org/t/p/original/" + picPath, 
				240, 
				360, 
				false, 
				false);
		
		newBox.getChildren().addAll(nameLabel, new ImageView(pic));
		newBox.getChildren().addAll(creditsButton);
		
		
		
		return newBox;
	}
	
	private void drawChart(){
		 final NumberAxis xAxis = new NumberAxis();
	        final NumberAxis yAxis = new NumberAxis();
	        xAxis.setLabel("Number of Month");
	        //creating the chart
	        final LineChart<Number,Number> lineChart = 
	                new LineChart<Number,Number>(xAxis,yAxis);
	                
	        lineChart.setTitle("Stock Monitoring, 2010");
	        //defining a series
	        XYChart.Series series = new XYChart.Series();
	        series.setName("My portfolio");
	        //populating the series with data
	        series.getData().add(new XYChart.Data(1, 23));
	        series.getData().add(new XYChart.Data(2, 14));
	        series.getData().add(new XYChart.Data(3, 15));
	        series.getData().add(new XYChart.Data(4, 24));
	        series.getData().add(new XYChart.Data(5, 34));
	        series.getData().add(new XYChart.Data(6, 36));
	        series.getData().add(new XYChart.Data(7, 22));
	        series.getData().add(new XYChart.Data(8, 45));
	        series.getData().add(new XYChart.Data(9, 43));
	        series.getData().add(new XYChart.Data(10, 17));
	        series.getData().add(new XYChart.Data(11, 29));
	        series.getData().add(new XYChart.Data(12, 25));
	        
	        lineChart.getData().add(series);
	        creditsPane.getChildren().addAll(lineChart);
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

	

	
