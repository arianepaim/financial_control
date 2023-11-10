package com.controlefinanceiro.financas.entities;

import com.controlefinanceiro.financas.shared.ExpensesDTO;
import com.controlefinanceiro.financas.shared.IncomeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reports {

    private List<ExpensesDTO> expenses;
    private List<IncomeDTO> income;
    private double balance;
}