package com.controlefinanceiro.financas.services;

import com.controlefinanceiro.financas.entities.Expenses;
import com.controlefinanceiro.financas.entities.exceptions.ResourceNotFoundException;
import com.controlefinanceiro.financas.repositories.ExpensesRepository;
import com.controlefinanceiro.financas.shared.ExpensesDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpensesService {

    @Autowired
    private ExpensesRepository expensesRepository;

    public List<ExpensesDTO> findAllByUserId(Long userId) {
        List<Expenses> list = expensesRepository.findAllByUserId(userId);
        return list
                .stream()
                .map(expenses -> new ModelMapper().map(expenses, ExpensesDTO.class))
                .collect(Collectors.toList());
    }


    public ExpensesDTO findById(Long id) {
        Expenses expenses = expensesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Despesas com o ID: " + id + " não encontrada"));
        return new ModelMapper().map(expenses, ExpensesDTO.class);
    }

    public ExpensesDTO insert(ExpensesDTO dto) {
        dto.setId(null);

        Expenses expenses = new ModelMapper().map(dto, Expenses.class);
        expenses = expensesRepository.save(expenses);
        dto.setId(expenses.getId());

        return dto;
    }

    public ExpensesDTO update(ExpensesDTO dto, Long id, Long userId) {
        if (!expensesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Despesas com o ID: " + id + " não encontrada");
        }
        dto.setId(id);
        dto.setUserId(userId);

        Expenses expenses = new ModelMapper().map(dto, Expenses.class);
        expenses = expensesRepository.save(expenses);

        return dto;
    }

    public boolean delete(Long id) {
        if (expensesRepository.existsById(id)) {
            expensesRepository.deleteById(id);
        } else {
            return false;
        }
        return true;
    }
}
