package movieproject;

import java.io.IOException;

import com.sun.glass.ui.Accessible.EventHandler;

import info.movito.themoviedbapi.model.people.Person;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class SummaryBoxV2 {

	VBox box;
	Label name;
	ImageView view;
	Person actor;
	
	public SummaryBoxV2() {
		super();
		FXMLLoader loader = new FXMLLoader(this.getClass()
				.getResource("/SummaryBox.fxml"));
		
		
		try {
		box = loader.load();
		} catch (IOException e) {
		throw new RuntimeException(e);
		}
	}
	
	
	public void addActor(Person p){
		actor = p;
		this.setName(actor.getName());
		box.getChildren().add(name);
		
	}
	
	public Person getActor(){
		return actor;
	}
	private void setName(String name){
		this.name = new Label(name);
		
		
	}
	
	public void add(Node n){
		box.getChildren().add(n);
	}
	
	public VBox getBox(){
		return box;
	}
	
	
	
}
