package com.controlefinanceiro.financas.services;

import com.controlefinanceiro.financas.entities.User;
import com.controlefinanceiro.financas.repositories.UserRepository;
import com.controlefinanceiro.financas.security.JWTService;
import com.controlefinanceiro.financas.shared.UserDTO;
import com.controlefinanceiro.financas.view.model.LoginResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final String hederPrefix = "Bearer ";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public List<UserDTO> findAll() {
        List<User> list = userRepository.findAll();
        return list.stream()
                .map(user -> new ModelMapper()
                        .map(user, UserDTO.class)).collect(Collectors.toList());
    }

    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário com o ID: " + id + " não encontrado."));
        return new ModelMapper().map(user, UserDTO.class);
    }

    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email não encontrado"));
        return new ModelMapper().map(user, UserDTO.class);
    }

    public UserDTO insert(UserDTO dto) {
        dto.setId(null);

        User user = new ModelMapper().map(dto, User.class);

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new InputMismatchException("Já existe usuário cadastrado com o email: " + user.getEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        dto.setId(user.getId());

        return dto;
    }

    public LoginResponse logInto(String email, String password) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password, Collections.emptyList()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = hederPrefix + jwtService.generateToken(authentication);

        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null) {
            return new LoginResponse(token, user);
        } else {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }
    }
}
