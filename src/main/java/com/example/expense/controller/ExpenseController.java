package com.example.expense.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.expense.model.Expense;
import com.example.expense.model.User;
import com.example.expense.repository.ExpenseRepository;
import com.example.expense.repository.UserRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class ExpenseController {
	
	@Autowired
	private ExpenseRepository expenseRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/expenses")
	Collection<Expense> getExpenses(){
		return expenseRepository.findAll();
	}
	
	@GetMapping("/expenses/{description}")
	ResponseEntity<?> getExpense(@PathVariable String description){
		Optional<Expense> expense = expenseRepository.findByDescription(description);
		return expense.map(response -> ResponseEntity.ok().body(response))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
		//return new ResponseEntity(response, HttpStatus.OK);
	}

	@Transactional
	@DeleteMapping("/expenses/{description}/users/{username}")
	ResponseEntity<?> deleteExpense(@PathVariable String description, @PathVariable String username)
	{
		Optional<User> user = userRepository.findByUsername(username);
		Set<Expense> setExp = new HashSet<>();
		for(Expense exp : user.get().getExpense()){
			if(!exp.getDescription().equals(description)){
				setExp.add(exp);
			}
		}
		user.get().setExpense(setExp);
		userRepository.save(user.get());
		expenseRepository.deleteByDescription(description);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/expenses")
	ResponseEntity<Expense> postExpense(@Valid @RequestBody Expense expense) throws URISyntaxException
	{
		Expense result = expenseRepository.save(expense);
		return ResponseEntity.created(new URI("/api/expenses" + result.getId())).body(result);
	}
	
	@PutMapping("/expenses/{id}")
	ResponseEntity<Expense> updateExpense(@Valid @RequestBody  Expense expense) throws URISyntaxException
	{
		
		Expense result = expenseRepository.save(expense);
		return ResponseEntity.ok().body(result);
				
	}
	
	@PostMapping("expenses/expense/users/{username}")
	ResponseEntity<?> postExpenseUser(@Valid @RequestBody Expense expense, @PathVariable String username) throws URISyntaxException
	{
		RestTemplate restT = new RestTemplate();
		final String URL1 = "http://localhost:8080/api/expenses";
		ResponseEntity<Expense> result = restT.postForEntity(URL1, expense, Expense.class);
		final String URL = "http://localhost:8080/api" + "/expenses/" + expense.getDescription();
		RestTemplate restTemplate = new RestTemplate();
	    Expense expenseResult = restTemplate.getForObject(URL, Expense.class);
		Optional<User> user = userRepository.findByUsername(username);
		Set<Expense> response = user.get().getExpense();
		response.add(expenseResult);
		user.get().setExpense(response);
		User result1 = userRepository.save(user.get());
		return ResponseEntity.ok().body(result1);
		
	}

}
 