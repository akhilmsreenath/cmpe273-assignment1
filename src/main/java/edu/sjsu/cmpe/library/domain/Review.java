package edu.sjsu.cmpe.library.domain;

import org.hibernate.validator.constraints.NotEmpty;
//import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Review {
	private long id;
	@NotEmpty
	@JsonProperty("rating")
	private int rating;
	
	@NotEmpty
	@JsonProperty("comment")
	private String comment;

	public long getId() {
		return id;
	    }
	
	public void setId(long id) {
		this.id = id;
	    }


	   
	    
	    public int  getRating() {
			return rating;
		    }
	    
	    public void setRating(int rating) {
			this.rating = rating;
	    }
	    
	    public String  getComment() {
			return comment;
		    }
	    
	    public void setComment(String comment) {
			this.comment = comment;
	    }
	    
	    

		    
 
}
