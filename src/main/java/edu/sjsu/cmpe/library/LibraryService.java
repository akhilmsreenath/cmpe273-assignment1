package edu.sjsu.cmpe.library;

import java.util.concurrent.ConcurrentHashMap;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

import edu.sjsu.cmpe.library.api.resources.BookResource;
import edu.sjsu.cmpe.library.api.resources.ReviewResource;
import edu.sjsu.cmpe.library.api.resources.RootResource;
import edu.sjsu.cmpe.library.config.LibraryServiceConfiguration;
import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.domain.Review;
import edu.sjsu.cmpe.library.repository.BookRepository;
import edu.sjsu.cmpe.library.repository.BookRepositoryInterface;
import edu.sjsu.cmpe.library.repository.ReviewRepository;
import edu.sjsu.cmpe.library.repository.ReviewRepositoryInterface;

public class LibraryService extends Service<LibraryServiceConfiguration> {

    public static void main(String[] args) throws Exception {
    	System.out.println("Inside Main");
	new LibraryService().run(args);
    }

    @Override
    public void initialize(Bootstrap<LibraryServiceConfiguration> bootstrap) {
	bootstrap.setName("library-service");
    }

    @Override
    public void run(LibraryServiceConfiguration configuration,
	    Environment environment) throws Exception {
	/** Root API */
	environment.addResource(RootResource.class);
	/** Books APIs */
	BookRepositoryInterface bookRepository = new BookRepository(
		new ConcurrentHashMap<Long, Book>());
	environment.addResource(new BookResource(bookRepository));
	
	ReviewRepositoryInterface reviewRepository = new ReviewRepository(
			new ConcurrentHashMap<Integer, Review>());
	environment.addResource(new ReviewResource(reviewRepository));
	//ReviewRepositoryInterface reviewRepository1 = new ReviewRepository(
		
	/** Add new resources here */
    }
}
