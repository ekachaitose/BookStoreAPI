package scb.challenge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import scb.challenge.model.request.UserRequest;
import scb.challenge.model.response.UserResponse;
import scb.challenge.service.UserService;

@RestController
@RequestMapping("/")
@Api(tags = "auth")
public class AuthController {

	@Autowired
	UserService userservice;

	@PostMapping("login")
	@ApiOperation(value = "This is the user login authentication API")
	public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
		UserResponse user = new UserResponse();
		user.setToken(userservice.login(userRequest.getUsername(), userRequest.getPassword()));
		user.setUsername(userRequest.getUsername());
		user.setPassword(userRequest.getPassword());
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
}
