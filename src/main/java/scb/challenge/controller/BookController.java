package scb.challenge.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import scb.challenge.model.Book;
import scb.challenge.model.response.BookResponse;
import scb.challenge.model.rest.BookClientResponse;
import scb.challenge.rest.BookRestClient;

@RestController
@RequestMapping("/books")
@Api(tags = "books")
public class BookController {

	@Autowired
	BookRestClient bookRestClient;

	@GetMapping("")
	@ApiOperation(value = "	Gets a list of books from an external book publisher’s webservices")
	public ResponseEntity<BookResponse> getUser(Principal principal) {
		BookResponse booksRespone = new BookResponse();
		List<BookClientResponse> books = this.bookRestClient.books();
		List<BookClientResponse> booksRecomment = this.bookRestClient.booksRecommendation();

		List<Book> lstBooks = new ArrayList<>();
		for (BookClientResponse bookRecomment : booksRecomment) {
			lstBooks.add(this.setBook(bookRecomment, true));
		}
		
		for (BookClientResponse book : books) {
			lstBooks.add(this.setBook(book, false));
		}


		booksRespone.setBooks(lstBooks);

		return new ResponseEntity<>(booksRespone, HttpStatus.OK);
	}

	private Book setBook(BookClientResponse bookClient, boolean isRecomment) {
		Book book = new Book();
		book.setId(bookClient.getId());
		book.set_recomended(isRecomment);
		book.setAuthor(bookClient.getAuthor_name());
		book.setName(bookClient.getBook_name());
		book.setPrice(bookClient.getPrice());
		return book;
	}
}
