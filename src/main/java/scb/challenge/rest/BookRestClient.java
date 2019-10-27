package scb.challenge.rest;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import scb.challenge.model.rest.BookClientResponse;

@Service
public class BookRestClient {

	public List<BookClientResponse> books() {
		RestTemplate restTemplate = new RestTemplate();
		String fooResourceUrl = "https://scb-test-book-publisher.herokuapp.com/books";
		ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl, String.class);
		if (response != null) {
			BookClientResponse[] bookClient = new Gson().fromJson(response.getBody(), BookClientResponse[].class);
			return  Arrays.asList(bookClient);
		}
		return null;
	}

	public List<BookClientResponse> booksRecommendation() {
		RestTemplate restTemplate = new RestTemplate();
		String fooResourceUrl = "https://scb-test-book-publisher.herokuapp.com/books/recommendation";
		ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl, String.class);
		if (response != null) {
			BookClientResponse[] bookClient = new Gson().fromJson(response.getBody(), BookClientResponse[].class);
			return  Arrays.asList(bookClient);
		}
		return null;
	}
}
