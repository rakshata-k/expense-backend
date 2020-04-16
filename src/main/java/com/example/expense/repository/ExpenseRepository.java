package com.example.expense.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.example.expense.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
	
	Optional<Expense> findByDescription(String description);

	@Transactional
	void deleteByDescription(String description);
}
