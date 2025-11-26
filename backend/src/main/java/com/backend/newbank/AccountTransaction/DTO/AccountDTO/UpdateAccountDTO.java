package com.backend.newbank.AccountTransaction.DTO.AccountDTO;




import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Date;

import com.backend.newbank.AccountTransaction.entity.Enum.Person_type;

public record UpdateAccountDTO(
        @Size(max = 64)
        String password, // Senha nova para atualizar

        Person_type personType, // Tipo de pessoa (não modificável mas incluído para referência)

        @Email
        String email, // Novo e-mail para atualizar

        BigDecimal monthly_Income,

        String phoneNumber, // Novo número de telefone

        Adress address, // Novo endereço
        
        NaturalPerson naturalPerson, // Dados da pessoa física, se aplicável

        LegalEntity legalEntity, // Dados da pessoa jurídica, se aplicável

        String accountNumber
) {

    public record NaturalPerson(
            String fullName, // Novo nome completo da pessoa física
            @Size(max = 11)
            String cpf, // CPF (não editável, incluído para referência)
            Date bornDate // Data de nascimento (não editável, incluído para referência)
    ) {
    }

    public record LegalEntity(
            String razaoSocial, // Nova razão social da pessoa jurídica
            String cnpj, // CNPJ (não editável, incluído para referência)
            String responsible // Novo responsável, se necessário
    ) {
    }

    public record Adress(
            String street, // Rua do endereço
            String neighborhood, // Bairro do endereço
            String cep, // CEP do endereço
            String number, // Número do endereço
            String state, // Estado do endereço
            String city, // Cidade do endereço
            String complement // Complemento do endereço
    ) {
    }
}


