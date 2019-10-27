package scb.challenge.entity;

import java.util.Date;

import lombok.Data;

@Data
public class UserEntity {

	Integer id;
	String username;
	String password;
	String name;
	String surname;
	Date birthdate;
	String status;;
	Date createdate;
	Date updateddate;
	String role;
	
	
	public String getRole() {
		return "user";
	}
}
