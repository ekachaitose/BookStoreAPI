package scb.challenge.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import scb.challenge.entity.UserEntity;
import scb.challenge.exception.ApiException;
import scb.challenge.repository.UserRepository;

@Service
public class MyUserDetails implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws ApiException {
		final UserEntity user = userRepository.findByUsername(username);

		if (user == null) {
			throw new ApiException("User '" + username + "' not found", HttpStatus.UNAUTHORIZED);
		}
		return org.springframework.security.core.userdetails.User//
				.withUsername(username)//
				.password(user.getPassword())//
				.authorities(user.getRole()).accountExpired(false)//
				.accountLocked(false)//
				.credentialsExpired(false)//
				.disabled(false)//
				.build();
	}

}
