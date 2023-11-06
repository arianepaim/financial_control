package com.controlefinanceiro.financas.shared;

import lombok.Data;

import java.util.Date;

@Data
public class IncomeDTO {

    private Long id;
    private String name;
    private String category;
    private Date date;
    private Double value;
    private Long userId;
}
