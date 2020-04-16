package com.example.expense.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.expense.repository.CategoryRepository;
import com.example.expense.model.Category;

@RestController
@RequestMapping("/api")
public class CategoryController {
	
	@Autowired
	private CategoryRepository categoryRepository;

	public CategoryController(CategoryRepository categoryRepository) {
		super();
		this.categoryRepository = categoryRepository;
	}
	
	@GetMapping("/categories")
	Collection<Category> categories(){
		return categoryRepository.findAll(); //equivalent to select * from category table
	}
	
	@GetMapping("/category/{name}")
	ResponseEntity<?> getCategory(@PathVariable String name){
		Optional<Category> category = categoryRepository.findByName(name);
		System.out.print(category);
		return category.map(response -> ResponseEntity.ok().body(response))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
		//return new ResponseEntity(response, HttpStatus.OK);
	}
	
	@PostMapping("/category")
	ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) throws URISyntaxException
	{
		
		Category result = categoryRepository.save(category); //insert into table category
		return ResponseEntity.created(new URI("/api/category/" + result.getId())).body(result); 
		
	}
	
	@PutMapping("/category/{name}")
	ResponseEntity<Category> updateCategory(@Valid @RequestBody Category category) throws URISyntaxException
	{
		
		Category result = categoryRepository.save(category);
		return ResponseEntity.ok().body(result);
				
	}
	
	@DeleteMapping("/category/{name}")
	ResponseEntity<?> deleteCategory(@PathVariable String name){
		categoryRepository.deleteByName(name);
		return ResponseEntity.ok().build();
	}
}
