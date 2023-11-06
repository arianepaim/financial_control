package com.controlefinanceiro.financas.view.model.income;

import lombok.Data;

import java.util.Date;

@Data
public class IncomeRequest {
    private String name;
    private String category;
    private Date date;
    private Double value;
    private Long userId;
}
