package com.controlefinanceiro.financas.repositories;

import com.controlefinanceiro.financas.entities.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findAllByUserId(Long userId);

    @Query(value = "SELECT * FROM tb_income WHERE user_id = :userId AND EXTRACT(MONTH FROM date) = :month", nativeQuery = true)
    List<Income> findAllByUserIdAndMonth(@Param("userId") Long userId, @Param("month") int month);
}
