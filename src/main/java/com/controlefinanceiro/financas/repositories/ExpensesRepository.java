package com.controlefinanceiro.financas.repositories;

import com.controlefinanceiro.financas.entities.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, Long> {
    List<Expenses> findAllByUserId(Long userId);
}
