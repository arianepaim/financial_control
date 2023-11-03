package com.controlefinanceiro.financas.shared;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class ExpensesDTO {

    private Long id;
    private String name;
    private String category;
    private Date date;
    private Double value;
    private Long userId;
}
