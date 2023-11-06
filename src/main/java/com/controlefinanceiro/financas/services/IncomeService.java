package com.controlefinanceiro.financas.services;

import com.controlefinanceiro.financas.entities.Expenses;
import com.controlefinanceiro.financas.entities.Income;
import com.controlefinanceiro.financas.entities.exceptions.ResourceNotFoundException;
import com.controlefinanceiro.financas.repositories.IncomeRepository;
import com.controlefinanceiro.financas.shared.ExpensesDTO;
import com.controlefinanceiro.financas.shared.IncomeDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    public List<IncomeDTO> findAllByUserId(Long userId) {
        List<Income> list = incomeRepository.findAllByUserId(userId);
        return list
                .stream()
                .map(income -> new ModelMapper().map(income, IncomeDTO.class))
                .collect(Collectors.toList());
    }


    public IncomeDTO findById(Long id) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receita com o ID: " + id + " não encontrada"));
        return new ModelMapper().map(income, IncomeDTO.class);
    }

    public IncomeDTO insert(IncomeDTO dto) {
        dto.setId(null);

        Income income = new ModelMapper().map(dto, Income.class);
        income = incomeRepository.save(income);
        dto.setId(income.getId());

        return dto;
    }

    public IncomeDTO update(IncomeDTO dto, Long id, Long userId) {
        if (!incomeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Receita com o ID: " + id + " não encontrada");
        }
        dto.setId(id);
        dto.setUserId(userId);

        Income income = new ModelMapper().map(dto, Income.class);
        income = incomeRepository.save(income);

        return dto;
    }

    public boolean delete(Long id) {
        if (incomeRepository.existsById(id)) {
            incomeRepository.deleteById(id);
        } else {
            return false;
        }
        return true;
    }
}
