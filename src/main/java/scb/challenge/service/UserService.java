package scb.challenge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import scb.challenge.entity.UserEntity;
import scb.challenge.exception.ApiException;
import scb.challenge.repository.UserRepository;
import scb.challenge.security.JwtTokenProvider;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private AuthenticationManager authenticationManager;

	public String login(String username, String password) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			return jwtTokenProvider.createToken(username);
		} catch (AuthenticationException e) {
			throw new ApiException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	public String signup(UserEntity user) {
		try {
			if (!userRepository.existsByUsername(user.getUsername())) {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				user.setRole("user");
				userRepository.save(user);
				return jwtTokenProvider.createToken(user.getUsername());
			} else {
				throw new ApiException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
			}
		} catch (Exception e) {
			throw new ApiException("Fail to create user try again later.", HttpStatus.EXPECTATION_FAILED);
		}
	}

	public void delete(String username) {
		try {
			if (!userRepository.deleteByUsername(username))
				throw new ApiException("The user doesn't exist", HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			throw new ApiException("Delete user fail try again later.", HttpStatus.EXPECTATION_FAILED);
		}
	}

	public UserEntity search(String username) {
		UserEntity user = userRepository.findByUsername(username);
		if (user == null) {
			throw new ApiException("The user doesn't exist", HttpStatus.NOT_FOUND);
		}
		return user;
	}

}
