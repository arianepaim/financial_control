package com.controlefinanceiro.financas.view.controller;

import com.controlefinanceiro.financas.entities.exceptions.ResourceNotFoundException;
import com.controlefinanceiro.financas.services.ExpensesService;
import com.controlefinanceiro.financas.shared.ExpensesDTO;
import com.controlefinanceiro.financas.view.model.expenses.ExpensesRequest;
import com.controlefinanceiro.financas.view.model.expenses.ExpensesResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expenses")
public class ExpensesController {

    @Autowired
    private ExpensesService service;

    @GetMapping
    public ResponseEntity<List<ExpensesResponse>> findAll() {
        List<ExpensesDTO> list = service.findAll();
        List<ExpensesResponse> responses = list
                .stream()
                .map(expensesDTO -> new ModelMapper().map(expensesDTO, ExpensesResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpensesResponse> findById(@PathVariable Long id) {
        ExpensesDTO dto = service.findById(id);
        ExpensesResponse response = new ModelMapper().map(dto, ExpensesResponse.class);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<ExpensesResponse> insert(@RequestBody ExpensesRequest request) {
        ExpensesDTO dto = new ModelMapper().map(request, ExpensesDTO.class);
        dto = service.insert(dto);
        ExpensesResponse response = new ModelMapper().map(dto, ExpensesResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpensesResponse> update(@RequestBody ExpensesRequest request, @PathVariable Long id) {
        ExpensesDTO dto = new ModelMapper().map(request, ExpensesDTO.class);
        dto = service.update(dto, id);
        ExpensesResponse response = new ModelMapper().map(dto, ExpensesResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        boolean deleted = service.delete(id);
        if (deleted) {
            return ResponseEntity.ok("Despesa deletada com sucesso.");
        } else {
            throw new ResourceNotFoundException("Despesas com o ID: " + id + " não encontrada.");
        }
    }
}
