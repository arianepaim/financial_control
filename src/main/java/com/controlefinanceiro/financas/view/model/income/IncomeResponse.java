package com.controlefinanceiro.financas.view.model.income;

import lombok.Data;

import java.util.Date;

@Data
public class IncomeResponse {

    private Long id;
    private String name;
    private String category;
    private Date date;
    private Double value;
}
