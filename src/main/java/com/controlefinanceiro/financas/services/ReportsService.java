package com.controlefinanceiro.financas.services;

import com.controlefinanceiro.financas.entities.Expenses;
import com.controlefinanceiro.financas.entities.Income;
import com.controlefinanceiro.financas.entities.Reports;
import com.controlefinanceiro.financas.repositories.ExpensesRepository;
import com.controlefinanceiro.financas.repositories.IncomeRepository;
import com.controlefinanceiro.financas.shared.ExpensesDTO;
import com.controlefinanceiro.financas.shared.IncomeDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportsService {

    @Autowired
    private ExpensesRepository expensesRepository;

    @Autowired
    private IncomeRepository incomeRepository;

    public Reports generateReports(Long userId) {
        List<Expenses> expensesList = expensesRepository.findAllByUserId(userId);
        List<Income> incomeList = incomeRepository.findAllByUserId(userId);

        List<ExpensesDTO> expensesDTOList = expensesList.stream()
                .map(expenses -> new ModelMapper().map(expenses, ExpensesDTO.class))
                .collect(Collectors.toList());

        List<IncomeDTO> incomeDTOList = incomeList.stream()
                .map(income -> new ModelMapper().map(income, IncomeDTO.class))
                .collect(Collectors.toList());

        double totalExpenses = expensesList.stream().mapToDouble(Expenses::getValue).sum();
        double totalIncome = incomeList.stream().mapToDouble(Income::getValue).sum();
        double balance = totalIncome - totalExpenses;

        Reports reports = new Reports();
        reports.setExpenses(expensesDTOList);
        reports.setIncome(incomeDTOList);
        reports.setBalance(balance);

        return reports;
    }

    public Reports generateReportsForMonth(Long userId, int month) {
        List<Expenses> expensesList = expensesRepository.findAllByUserIdAndMonth(userId, month);
        List<Income> incomeList = incomeRepository.findAllByUserIdAndMonth(userId, month);

        List<ExpensesDTO> expensesDTOList = expensesList.stream()
                .map(expenses -> new ModelMapper().map(expenses, ExpensesDTO.class))
                .collect(Collectors.toList());

        List<IncomeDTO> incomeDTOList = incomeList.stream()
                .map(income -> new ModelMapper().map(income, IncomeDTO.class))
                .collect(Collectors.toList());

        double totalExpenses = expensesList.stream().mapToDouble(Expenses::getValue).sum();
        double totalIncome = incomeList.stream().mapToDouble(Income::getValue).sum();
        double balance = totalIncome - totalExpenses;

        Reports reports = new Reports();
        reports.setExpenses(expensesDTOList);
        reports.setIncome(incomeDTOList);
        reports.setBalance(balance);

        return reports;
    }
}
