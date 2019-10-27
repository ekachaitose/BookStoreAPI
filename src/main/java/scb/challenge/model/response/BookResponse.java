package scb.challenge.model.response;

import java.util.List;

import lombok.Data;
import scb.challenge.model.Book;

@Data
public class BookResponse {
	List<Book> books;
}
