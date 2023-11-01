package com.controlefinanceiro.financas.view.model;

import com.controlefinanceiro.financas.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private User user;
}
