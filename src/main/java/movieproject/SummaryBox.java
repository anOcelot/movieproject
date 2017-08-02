package movieproject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import info.movito.themoviedbapi.model.people.Person;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

public class SummaryBox extends VBox implements Initializable{
	
	
	Label name;
	ImageView view;
	Person actor;
	
	
	
	public SummaryBox() {
		
		super();
		FXMLLoader loader = new FXMLLoader(this.getClass()
				.getResource("/SummaryBox.fxml"));
		
		loader.setController(this);
		try {
		loader.load();
		} catch (IOException e) {
		throw new RuntimeException(e);
		}
	}
	
	public void addActor(Person p){
		actor = p;
		this.setName(actor.getName());
	}
	
	public Person getActor(){
		return actor;
	}
	private void setName(String name){
		this.name = new Label(name);
		
		
	}
	
	
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}
	
	
	
	

}
