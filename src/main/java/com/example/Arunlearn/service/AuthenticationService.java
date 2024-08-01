package com.example.Arunlearn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Arunlearn.model.AuthenticationResponse;
import com.example.Arunlearn.model.User;
import com.example.Arunlearn.repository.UserRepository;

@Service
public class AuthenticationService {

	@Autowired
	private UserRepository userrepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtservice;

	@Autowired
	private AuthenticationManager authenticationManager;

	public ResponseEntity<?> register(User user) {

		if (userrepo.findByUsername(user.getUsername()).isPresent()) {

			return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("UserName Already Exist");
		}

		User users = new User();
		users.setFirst_name(user.getFirst_name());
		users.setLast_name(user.getLast_name());

		System.out.println(user.getUsername() + "Username printed ");

		System.out.println(user.getPassword() + "Password printed ");
		users.setUsername(user.getUsername());

		users.setPassword(passwordEncoder.encode(user.getPassword()));
		System.out.println(users.getUsername() + "Username printed ");

		System.out.println(users.getPassword() + "Password printed ");

		users.setRole(user.getRole());

		users = userrepo.save(users);

		String accessToken = jwtservice.generateAccessToken(users);

		return ResponseEntity.status(HttpStatus.OK).body(new AuthenticationResponse(accessToken));

	}

	public AuthenticationResponse authenticate(User request) {
		
		if(userrepo.findByUsername(request.getUsername()).isEmpty()) {
			
		 return	new AuthenticationResponse("UserName is Not Register , Please register the username");
		}
		
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		User user = userrepo.findByUsername(request.getUsername()).orElseThrow(
				() -> new UsernameNotFoundException("UserName is Not Register , Please register the username"));
		String accessToken = jwtservice.generateAccessToken(user);

		return new AuthenticationResponse(accessToken);

	}

}
