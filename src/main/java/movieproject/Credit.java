package movieproject;

import com.fasterxml.jackson.annotation.JsonProperty;

import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.people.PersonCredit;

public class Credit extends PersonCredit {
	
	
	@JsonProperty("revenue")
	private long revenue;
	
	public Credit(PersonCredit c, TmdbMovies movies){
		
		
	}

}
