package scb.challenge.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class OrderEntity {

	Integer id;
	String orderNo;
	Integer bookId;
	String bookName;
	Integer userId;
	BigDecimal bookPrice;
	Date createdate;
}
