package scb.challenge.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Book {
	Integer id;
	String name;
	String author;
	BigDecimal price;
	boolean is_recomended;
}
