package edu.sjsu.cmpe.library.api.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.yammer.dropwizard.jersey.params.LongParam;
import com.yammer.metrics.annotation.Timed;

import edu.sjsu.cmpe.library.domain.Author;
import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.dto.AuthorDto;
//import edu.sjsu.cmpe.library.domain.BookJ;
import edu.sjsu.cmpe.library.dto.BookDto;
import edu.sjsu.cmpe.library.dto.LinkDto;
import edu.sjsu.cmpe.library.dto.LinksDto;
import edu.sjsu.cmpe.library.repository.BookRepositoryInterface;

@Path("/v1/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    /** bookRepository instance */
    private final BookRepositoryInterface bookRepository;

    /**
     * BookResource constructor
     * 
     * @param bookRepository
     *            a BookRepository instance
     */
    public BookResource(BookRepositoryInterface bookRepository) {
	this.bookRepository = bookRepository;
    }

    @GET
    @Path("/{isbn}")
    @Timed(name = "view-book")
    public BookDto getBookByIsbn(@PathParam("isbn") LongParam isbn) {
    	System.out.println("inside isbn");
	Book book = bookRepository.getBookByISBN(isbn.get());
	BookDto bookResponse = new BookDto(book);
	String location = "/books/" + book.getIsbn();
	bookResponse.addLink(new LinkDto("view-book", location,
		"GET"));
	bookResponse.addLink(new LinkDto("update-book",
			location, "PUT"));
	bookResponse.addLink(new LinkDto("delete-book",
			location, "DELETE"));
	bookResponse.addLink(new LinkDto("create-review",
			location, "POST"));
	// add more links

	return bookResponse;
    }

    @POST
    
    @Timed(name = "create-book")

    public Response createBook(Book book) {
    	System.out.println("It is comming insude Book resource");
	// Store the new book in the BookRepository so that we can retrieve it.
    	
    //book.setIsbn(book.get);	
	Book savedBook = bookRepository.saveBook(book);
	String location = "/books/" + savedBook.getIsbn();
	System.out.println("Location:  "+location);
	//BookDto bookResponse = new BookDto(savedBook);
	LinksDto bookResponse = new LinksDto();
	bookResponse.addLink(new LinkDto("view-book", location, "GET"));
	bookResponse.addLink(new LinkDto("update-book", location, "PUT"));
	bookResponse.addLink(new LinkDto("delete-book", location, "DELETE"));
	bookResponse.addLink(new LinkDto("Create-review", location, "POST"));
	// Add other links if needed

	return Response.status(201).entity(bookResponse).build();}
	
	
		

	//	return null;
    
    
    @DELETE
    @Path("/{isbn}")
    @Timed(name = "delete-book")
    public Response removeBookByIsbn(@PathParam("isbn") Long isbn) {
    	System.out.println("inside isbn");
	Book bookresponse = bookRepository.removeBookByISBN(isbn);
	System.out.println(bookresponse);
	if(bookresponse==null)
		return Response.status(400).entity("wrong ISBN number").build();
	else
	{
		
		LinksDto links = new LinksDto();
		links.addLink(new LinkDto("create-book", "/books", "POST"));
		return Response.ok(links).build();
	}
	    }
    @PUT
	@Path("/{isbn}")
	@Timed(name = "update-book")
	public Response updateBook(@PathParam("isbn") LongParam isbn, @Context UriInfo uriInfo, String content) throws Exception {

		MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
		Book updateResponse = bookRepository.getBookByISBN(isbn.get());
		for(Map.Entry<String, List<String>> entry : queryParams.entrySet()){
			bookRepository.updateBookInfo(updateResponse, entry);
		}
		LinksDto links = new LinksDto();
		links.addLink(new LinkDto("view-book", "/books/" + isbn.toString() , "GET"));
		links.addLink(new LinkDto("update-book", "/books/" + isbn.toString() , "PUT"));
		links.addLink(new LinkDto("create-book", "/books/" + isbn.toString() , "DELETE"));
		links.addLink(new LinkDto("create-book", "/books/" + isbn.toString() + "/reviews", "POST"));
		links.addLink(new LinkDto("create-book", "/books", "POST"));

		return Response.ok(links).build();
	}

	@GET
	@Path("/{isbn}/authors/{id}")
	@Timed(name = "view-book-author")
	public AuthorDto viewBookAuthor(@PathParam("isbn") LongParam isbn, @PathParam("id") int id) {
		Book book = bookRepository.getBookByISBN(isbn.get());
		Author author = book.getAuthors().get(id-1);
		AuthorDto authorResponse = new AuthorDto(author);
		authorResponse.addLink(new LinkDto("view-author", 
				"/books/" + book.getIsbn() + "/authors/" + author.getId(), "GET"));
		return authorResponse;
	}

	@GET
	@Path("/{isbn}/authors")
	@Timed(name = "view-book-author")
	public ArrayList<Author> viewAllAuthors(@PathParam("isbn") LongParam isbn) {
		Book book = bookRepository.getBookByISBN(isbn.get());
		ArrayList<Author> authors = book.getAuthors();
		return authors;
	}
}

