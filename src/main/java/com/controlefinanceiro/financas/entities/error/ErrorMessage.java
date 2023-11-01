package com.controlefinanceiro.financas.entities.error;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorMessage {

    private Date dateNow;
    private String title;
    private int status;
    private String message;

    public ErrorMessage(String title, int status, String message) {
        this.dateNow = new Date();
        this.title = title;
        this.status = status;
        this.message = message;
    }
}
