package com.controlefinanceiro.financas.view.model.expenses;

import lombok.Data;

import java.util.Date;

@Data
public class ExpensesRequest {

    private String name;
    private String category;
    private Date date;
    private Double value;
    private Long userId;
}
