package com.example.Arunlearn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Arunlearn.model.AuthenticationResponse;
import com.example.Arunlearn.model.User;
import com.example.Arunlearn.service.AuthenticationService;

@RestController
public class MainController {
	
	@Autowired
	private AuthenticationService authService;
	
	@PostMapping("/register")
	public ResponseEntity<?> getData(@RequestBody User user) {
		System.out.println("get the data");
		return authService.register(user);
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> getLogin(@RequestBody User user) {
		System.out.println("get the Login");
		return ResponseEntity.ok(authService.authenticate(user));
	}
	@GetMapping("/get")
	public ResponseEntity<String> getDatas(){
		
		return ResponseEntity.ok("Hello from User");
	}

}
