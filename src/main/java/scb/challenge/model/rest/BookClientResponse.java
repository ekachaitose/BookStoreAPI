package scb.challenge.model.rest;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BookClientResponse {
	Integer id;
	String author_name;
	String book_name;
	BigDecimal price;
}
