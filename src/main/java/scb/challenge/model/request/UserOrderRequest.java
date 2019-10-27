package scb.challenge.model.request;

import java.util.List;

import lombok.Data;

@Data
public class UserOrderRequest {
	List<Integer> orders;
}
