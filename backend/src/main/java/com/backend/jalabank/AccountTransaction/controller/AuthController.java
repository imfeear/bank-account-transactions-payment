package com.backend.jalabank.AccountTransaction.controller;

import com.backend.jalabank.AccountTransaction.DTO.AuthDTOS.CadastroRequestDTO;
import com.backend.jalabank.AccountTransaction.DTO.AuthDTOS.LoginRequestDTO;
import com.backend.jalabank.AccountTransaction.DTO.AuthDTOS.ResponseLoginDTO;
import com.backend.jalabank.AccountTransaction.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseLoginDTO> login(@RequestBody @Valid LoginRequestDTO body) {
        return ResponseEntity.status(200).body(authService.login(body));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody CadastroRequestDTO body) {
        Map<String, String> response = authService.register(body);

        if (response.get("message").contains("sucesso")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @PostMapping("/confirmAuth")
    public ResponseEntity<Map<String, String>> confirmAuth(@RequestBody LoginRequestDTO body) {
        boolean isConfrimed = authService.confirmAuthentication(body);
        Map<String, String> response = new HashMap<>();

        if (isConfrimed) {
            response.put("message", "Authenticated user");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            response.put("message", "Unauthenticated user");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
