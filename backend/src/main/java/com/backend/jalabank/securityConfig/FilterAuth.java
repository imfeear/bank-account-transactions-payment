package com.backend.jalabank.securityConfig;

import com.backend.jalabank.AccountTransaction.entity.Account;
import com.backend.jalabank.AccountTransaction.repository.AccountRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class FilterAuth extends OncePerRequestFilter {

    @Autowired
    TokenJWTService tokenJWTService;

    @Autowired
    AccountRepository accountRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = this.getJwt(request);

        if (token != null && tokenJWTService.validateToken(token) != null) {
            Long accountId = Long.valueOf(tokenJWTService.validateToken(token));

            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new RuntimeException("Conta não encontrada"));


            var authentication = new UsernamePasswordAuthenticationToken(account.getId(), null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }


    private String getJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        }
        return null;
    }
}
