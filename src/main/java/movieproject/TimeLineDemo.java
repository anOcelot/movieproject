package movieproject;

//import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.NumberFormat;

import java.util.Iterator;

import java.util.List;


import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbFind;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbPeople;
import info.movito.themoviedbapi.TmdbPeople.PersonResultsPage;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.Artwork;
import info.movito.themoviedbapi.model.Video;
import info.movito.themoviedbapi.model.core.SessionToken;
import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.people.PersonCredit;
import info.movito.themoviedbapi.model.people.PersonCredits;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Main class instantiates a gui which allows the users enter an actor's
 * name, then uses this user input to generate an ActorTimeLine for that actor
 * and present a visualization.
 * @author Pieter Holleman, Zachary Hern, Adam Slifco
 * @version 2
 * @since 8-7-2017
 */
public class TimeLineDemo extends Application {
	
	/** movie TmdbMovies database for grabbing info*/
	private TmdbMovies movies;
	
	private TmdbFind findVideo;
	
	private Video trailer;
	
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
	
	/**data series for currently selected actor */
	XYChart.Series currentSeries;
	
	/**api and sessionToken */
	TmdbApi tmdbApi;
	SessionToken sessionToken;

	/**
	 * when this method is called, JavaFX instantiates and arranges
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
	 * Basic search method searches for an actor by name,
	 * starts a thread rendering the results.
	 * @param str The name of the actor to search for
	 */
	private void search(final String str) {
		
		
		
		/** clear any previous results pane **/
		resultsPane.getChildren().clear();
		
		/** first, find the people. */
		PersonResultsPage results = tmdbSearch
				.searchPerson(str, true, 0);
		
		/** Iterator to access results */
		Iterator<Person> iterator = results.iterator();
			
		
		Person currentActor;
		
		while (iterator.hasNext()){
		currentActor = iterator.next();
		resultsPane.getChildren().addAll(drawActorBox(currentActor));
		
		}
		
	}
	
	private class ChartTask implements Runnable {
		
		Thread thread;
		LineChart chart;
		public void run() {
			try{
			chart = drawChart();
			System.out.println("chart thread successful");
			} catch (Exception e){
				e.printStackTrace();
			}
		
			Platform.runLater(new Runnable(){
				public void run() {
					chart.setMinSize(creditsPane.getWidth(), creditsPane.getHeight());
					creditsPane.getChildren().add(chart);
				}
			});
		}
		
		public void start(){
			
			if (thread == null){
				thread = new Thread(this, "credit search");
				thread.start();
			}
		}
		
	}
	

	private class CreditTask implements Runnable {
		
		Thread thread;
		
		
		public void drawCredits(){
			
			PersonCredits credits = tmdbPeople
					.getPersonCredits(actor.getId());
			ActorTimeLine sortedCredits = new ActorTimeLine(credits, movies);
			for (PersonCredit c: sortedCredits.getCast()){
			
			
			
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
			creditsPane.getChildren().clear();
			actor = p;
			CreditTask creditThread = new CreditTask();
			creditThread.start();
			ChartTask c = new ChartTask();
			c.start();
			
		}
		
		
	}
	
	

	/**
	 * Main method launches the Application.
	 * @param args 
	 */
	public static void main(String[] args) {
		
		launch(args);

	}
	
	
	

	private VBox drawFilmBox(final PersonCredit credit){
		
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
		
		
		newBox.setOnMouseClicked(new EventHandler<MouseEvent>(){
			
			//@Override
			public void handle(MouseEvent event){
				
				try {
					playVideo(credit.getMovieId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
		
		
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
	
	private LineChart drawChart(){
		 final NumberAxis yAxis = new NumberAxis();
	        //final NumberAxis xAxis = new StringAxis("Year", 1900, 2017, 1);
	      
	        final CategoryAxis xAxis2 = new CategoryAxis();
	        //creating the chart
	        final LineChart<String,Number> lineChart = 
	                new LineChart<String,Number>(xAxis2,yAxis);
	        lineChart.setTitle("Career of " + actor.getName());
	        
	        PersonCredits credits = tmdbPeople
					.getPersonCredits(actor.getId());
			ActorTimeLine sortedCredits = new ActorTimeLine(credits, movies);
			XYChart.Series series = new XYChart.Series();
			series.setName("Career Earnings");
			for (PersonCredit c: sortedCredits.getCast()){
	                
	        String fulldate = c.getReleaseDate();
			final long revenue = movies.getMovie(c.getId(), "english").getRevenue();
			//String[] releaseDate = c.getReleaseDate().split("-");
			final int date;

			if (fulldate != null && revenue > 0) {
				String[] releaseDate = fulldate.split("-");
				if (releaseDate.length == 3){
					XYChart.Data nextData = new XYChart.Data(releaseDate[0], revenue);
					series.getData().add(nextData);
				}
			}
			
			
			}
			
	        lineChart.getData().add(series);
//	        creditsPane.getChildren().addAll(lineChart);
	        return lineChart;
	}
	
	 public void playVideo(int id) throws Exception {
		 
		 Stage stage = new Stage(StageStyle.DECORATED);
		
		trailer = movies.getVideos(id, "english").get(0);
		//System.out.println("https://www.youtube.com/watch?v=" + trailer.getKey());
		 WebView webview = new WebView();
	    webview.getEngine().load(
	    	 "https://www.youtube.com/watch?v=" + trailer.getKey()
	    );
	    webview.setPrefSize(640, 390);
	    
	    stage.setScene(new Scene(webview));
	    stage.show();
	    
	    
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

	

	
