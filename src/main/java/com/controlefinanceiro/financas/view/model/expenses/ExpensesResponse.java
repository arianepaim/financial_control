package com.controlefinanceiro.financas.view.model.expenses;

import lombok.Data;

import java.util.Date;

@Data
public class ExpensesResponse {

    private Long id;
    private String name;
    private String category;
    private Date date;
    private Double value;
}
