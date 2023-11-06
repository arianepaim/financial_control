package com.controlefinanceiro.financas.view.controller;

import com.controlefinanceiro.financas.entities.exceptions.ResourceNotFoundException;
import com.controlefinanceiro.financas.services.ExpensesService;
import com.controlefinanceiro.financas.shared.ExpensesDTO;
import com.controlefinanceiro.financas.shared.UserDTO;
import com.controlefinanceiro.financas.view.model.expenses.ExpensesRequest;
import com.controlefinanceiro.financas.view.model.expenses.ExpensesResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((UserDTO) authentication.getPrincipal()).getId();

        List<ExpensesDTO> list = service.findAllByUserId(userId);

        List<ExpensesResponse> responses = list
                .stream()
                .map(expensesDTO -> new ModelMapper().map(expensesDTO, ExpensesResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpensesResponse> findById(@PathVariable Long id) {
        ExpensesDTO dto = service.findById(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((UserDTO) authentication.getPrincipal()).getId();

        if (!dto.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        ExpensesResponse response = new ModelMapper().map(dto, ExpensesResponse.class);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<ExpensesResponse> insert(@RequestBody ExpensesRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((UserDTO) authentication.getPrincipal()).getId();

        ExpensesDTO dto = new ModelMapper().map(request, ExpensesDTO.class);
        dto.setUserId(userId);
        dto = service.insert(dto);

        ExpensesResponse response = new ModelMapper().map(dto, ExpensesResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpensesResponse> update(@RequestBody ExpensesRequest request, @PathVariable Long id) {
        ExpensesDTO existingExpense = service.findById(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((UserDTO) authentication.getPrincipal()).getId();

        if (!existingExpense.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        ExpensesDTO dto = new ModelMapper().map(request, ExpensesDTO.class);
        dto = service.update(dto, id, userId);

        ExpensesResponse response = new ModelMapper().map(dto, ExpensesResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        ExpensesDTO existingExpense = service.findById(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((UserDTO) authentication.getPrincipal()).getId();

        if (!existingExpense.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para excluir essa despesa.");
        }

        boolean deleted = service.delete(id);
        if (deleted) {
            return ResponseEntity.ok("Despesa deletada com sucesso.");
        } else {
            throw new ResourceNotFoundException("Despesas com o ID: " + id + " não encontrada.");
        }
    }
}
