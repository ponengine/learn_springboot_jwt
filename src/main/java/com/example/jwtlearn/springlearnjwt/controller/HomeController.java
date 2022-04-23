package com.example.jwtlearn.springlearnjwt.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwtlearn.springlearnjwt.model.AuthenticationRequest;
import com.example.jwtlearn.springlearnjwt.model.AuthenticationResponse;
import com.example.jwtlearn.springlearnjwt.service.MyUserDetailService;
import com.example.jwtlearn.springlearnjwt.util.JwtUtil;

@RestController
public class HomeController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private MyUserDetailService userDetailService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@GetMapping({"/hello","/index"})
	public String hello() {
		return "Hello World";
	
	}
	
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		}catch(BadCredentialsException e) {
			throw new Exception("Incorrect username or password",e);
		}
		
		final UserDetails userDetails = userDetailService.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt = "Bearer "+jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
				}
}
