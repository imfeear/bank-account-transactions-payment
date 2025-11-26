package com.backend.newbank.AccountTransaction.DTO.AuthDTOS;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Date;

import com.backend.newbank.AccountTransaction.entity.Enum.Person_type;

public record CadastroRequestDTO(
        @Nonnull
        @Size(max = 64)
        String password,

        @Nonnull
        Person_type personType,

        @Nonnull
        @Email
        String email,

        @Nonnull
        String phone_number,

        BigDecimal monthly_income,

        @Nonnull
        Adress adress,

        NaturalPerson naturalPerson,
        Legal_entity legalEntity) {

    public record NaturalPerson(
            @Nonnull
            String full_name,

            @Nonnull
            @Size(max = 11)
            String cpf,

            @Nonnull
            Date born_date) {
    }

    public record Legal_entity(
            @Nonnull
            String razao_social,

            @Nonnull
            String cnpj,

            @Nonnull
            String responsible) {
    }

    public record Adress(
            @Nonnull
            String street,

            @Nonnull
            String neighborhood,

            @Nonnull
            String cep,

            String number,

            @Nonnull
            String state,

            @Nonnull
            String city,

            String complement
    ) {
    }
}
