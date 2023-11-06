package com.controlefinanceiro.financas.view.controller;

import com.controlefinanceiro.financas.entities.exceptions.ResourceNotFoundException;
import com.controlefinanceiro.financas.services.ExpensesService;
import com.controlefinanceiro.financas.services.IncomeService;
import com.controlefinanceiro.financas.shared.ExpensesDTO;
import com.controlefinanceiro.financas.shared.IncomeDTO;
import com.controlefinanceiro.financas.shared.UserDTO;
import com.controlefinanceiro.financas.view.model.expenses.ExpensesRequest;
import com.controlefinanceiro.financas.view.model.expenses.ExpensesResponse;
import com.controlefinanceiro.financas.view.model.income.IncomeRequest;
import com.controlefinanceiro.financas.view.model.income.IncomeResponse;
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
@RequestMapping("/api/incomes")
public class IncomeController {

    @Autowired
    private IncomeService service;

    @GetMapping
    public ResponseEntity<List<IncomeResponse>> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((UserDTO) authentication.getPrincipal()).getId();

        List<IncomeDTO> list = service.findAllByUserId(userId);

        List<IncomeResponse> responses = list
                .stream()
                .map(incomeDTO -> new ModelMapper().map(incomeDTO, IncomeResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomeResponse> findById(@PathVariable Long id) {
        IncomeDTO dto = service.findById(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((UserDTO) authentication.getPrincipal()).getId();

        if (!dto.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        IncomeResponse response = new ModelMapper().map(dto, IncomeResponse.class);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<IncomeResponse> insert(@RequestBody IncomeRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((UserDTO) authentication.getPrincipal()).getId();

        IncomeDTO dto = new ModelMapper().map(request, IncomeDTO.class);
        dto.setUserId(userId);
        dto = service.insert(dto);

        IncomeResponse response = new ModelMapper().map(dto, IncomeResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncomeResponse> update(@RequestBody IncomeRequest request, @PathVariable Long id) {
        IncomeDTO existingIncome = service.findById(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((UserDTO) authentication.getPrincipal()).getId();

        if (!existingIncome.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        IncomeDTO dto = new ModelMapper().map(request, IncomeDTO.class);
        dto = service.update(dto, id, userId);

        IncomeResponse response = new ModelMapper().map(dto, IncomeResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        IncomeDTO existingIncome = service.findById(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((UserDTO) authentication.getPrincipal()).getId();

        if (!existingIncome.getUserId().equals(userId)) {
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
