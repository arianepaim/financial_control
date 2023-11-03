package com.controlefinanceiro.financas.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "tb_expenses")
public class Expenses {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 255)
    private String name;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 255)
    private String category;
    @NotNull
    @NotBlank
    private Date date;
    @NotNull
    private Double value;
    @NotNull
    private Long userId;
}
