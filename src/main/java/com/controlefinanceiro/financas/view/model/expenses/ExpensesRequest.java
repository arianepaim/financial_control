package com.controlefinanceiro.financas.view.model.expenses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ExpensesRequest {

    private String name;
    private String category;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private Date date;
    private Double value;
}
