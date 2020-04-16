package com.example.expense.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.expense.model.User;
import com.example.expense.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/users")
	Collection<User> getUsers(){
		return userRepository.findAll();
	}
	
	@GetMapping("/users/{username}")
	ResponseEntity<?> getUser(@PathVariable String username){
		Optional<User> user = userRepository.findByUsername(username);
		return user.map(response -> ResponseEntity.ok().body(response))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
		//return new ResponseEntity(response, HttpStatus.OK);
	}

	
	@PostMapping("/users")
	ResponseEntity<User> postUser(@Valid @RequestBody User user) throws URISyntaxException
	{
		User result = userRepository.save(user);
		return ResponseEntity.created(new URI("/api/users" + result.getUsername())).body(result);
	}

}
