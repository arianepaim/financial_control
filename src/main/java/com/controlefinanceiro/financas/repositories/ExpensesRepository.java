package com.controlefinanceiro.financas.repositories;

import com.controlefinanceiro.financas.entities.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, Long> {
    List<Expenses> findAllByUserId(Long userId);
    @Query(value = "SELECT * FROM tb_expenses WHERE user_id = :userId AND EXTRACT(MONTH FROM date) = :month", nativeQuery = true)
    List<Expenses> findAllByUserIdAndMonth(@Param("userId") Long userId, @Param("month") int month);
}
