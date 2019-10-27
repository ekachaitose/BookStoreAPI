package scb.challenge.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;
import scb.challenge.model.request.UserRequest;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(Include.NON_NULL)
public class UserResponse extends UserRequest{
	String token;
	List<Integer> books;
	
}
