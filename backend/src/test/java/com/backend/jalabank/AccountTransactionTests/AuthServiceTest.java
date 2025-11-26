package com.backend.jalabank.AccountTransactionTests;

import com.backend.jalabank.AccountTransaction.DTO.AuthDTOS.CadastroRequestDTO;
import com.backend.jalabank.AccountTransaction.DTO.AuthDTOS.LoginRequestDTO;
import com.backend.jalabank.AccountTransaction.DTO.AuthDTOS.ResponseLoginDTO;
import com.backend.jalabank.AccountTransaction.entity.Enum.Person_type;
import com.backend.jalabank.AccountTransaction.repository.AdressRepository;
import com.backend.jalabank.AccountTransaction.repository.LegalEntityRespository;
import com.backend.jalabank.AccountTransaction.repository.NaturalPersonRepository;
import com.backend.jalabank.AccountTransaction.repository.PersonRepository;
import com.backend.jalabank.AccountTransaction.service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

@SpringBootTest
@Transactional
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private NaturalPersonRepository naturalPersonRepository;

    @Autowired
    private LegalEntityRespository legalEntityRepository;

    @Autowired
    private AdressRepository adressRepository;

    @Test
    public void testRegisterNewNaturalPerson() {
        Date birthDate = java.sql.Date.valueOf(LocalDate.of(1990, 1, 1));
        CadastroRequestDTO cadastroRequestDTO = new CadastroRequestDTO(
                "12345678",
                Person_type.fisica,
                "example@email.com",
                "1234567333",
                BigDecimal.valueOf(5000.00),
                new CadastroRequestDTO.Adress(
                        "Rua Exemplo",
                        "Centro",
                        "12345678",
                        "123",
                        "SP",
                        "São Paulo",
                        "Apto 101"
                ),
                new CadastroRequestDTO.NaturalPerson(
                        "Djordan",
                        "12145678933",
                        birthDate
                ),
                null
        );

        // Chama o serviço de registro
        authService.register(cadastroRequestDTO);

        // Verifica se a pessoa foi salva corretamente
        var savedPerson = personRepository.findByEmail(cadastroRequestDTO.email());
        Assertions.assertTrue(savedPerson.isPresent(), "A pessoa não foi encontrada no banco de dados.");
        Assertions.assertEquals(cadastroRequestDTO.email(), savedPerson.get().getEmail(), "Os e-mails não correspondem.");
        Assertions.assertEquals(Person_type.fisica, savedPerson.get().getPerson_Type(), "O tipo de pessoa não está correto.");

        // Verifica a existência da pessoa física
        var savedNaturalPerson = naturalPersonRepository.findByCpf(cadastroRequestDTO.naturalPerson().cpf());
        Assertions.assertTrue(savedNaturalPerson.isPresent(), "A pessoa física não foi encontrada no banco de dados.");
        Assertions.assertEquals(cadastroRequestDTO.naturalPerson().cpf(), savedNaturalPerson.get().getCpf(), "Os CPFs não correspondem.");

        // Verifica o endereço associado
        var savedAdress = adressRepository.findByPerson_id(savedPerson.get().getId());
        Assertions.assertTrue(savedAdress.isPresent(), "O endereço não foi encontrado.");
        Assertions.assertEquals(cadastroRequestDTO.adress().street(), savedAdress.get().getStreet(), "Os endereços não correspondem.");
    }

    @Test
    public void testRegisterNewLegalEntity() {
        CadastroRequestDTO cadastroRequestDTO = new CadastroRequestDTO(
                "12345678",
                Person_type.juridica,
                "company@example.com",
                "9876543210",
                BigDecimal.valueOf(10000.00),
                new CadastroRequestDTO.Adress(
                        "Avenida Empresa",
                        "Comercial",
                        "98765432",
                        "456",
                        "RJ",
                        "Rio de Janeiro",
                        "Sala 202"
                ),
                null,
                new CadastroRequestDTO.Legal_entity(
                        "Empresa Exemplo Ltda",
                        "9876543210001",
                        "João Silva"
                )
        );

        // Chama o serviço de registro
        authService.register(cadastroRequestDTO);

        // Verifica se a pessoa jurídica foi salva corretamente
        var savedPerson = personRepository.findByEmail(cadastroRequestDTO.email());
        Assertions.assertTrue(savedPerson.isPresent(), "A pessoa jurídica não foi encontrada no banco de dados.");
        Assertions.assertEquals(cadastroRequestDTO.email(), savedPerson.get().getEmail(), "Os e-mails não correspondem.");
        Assertions.assertEquals(Person_type.juridica, savedPerson.get().getPerson_Type(), "O tipo de pessoa não está correto.");

        // Verifica a existência da entidade legal
        var savedLegalEntity = legalEntityRepository.findByCnpj(cadastroRequestDTO.legalEntity().cnpj());
        Assertions.assertTrue(savedLegalEntity.isPresent(), "A entidade legal não foi encontrada.");
        Assertions.assertEquals(cadastroRequestDTO.legalEntity().cnpj(), savedLegalEntity.get().getCnpj(), "Os CNPJs não correspondem.");

        // Verifica o endereço associado
        var savedAdress = adressRepository.findByPerson_id(savedPerson.get().getId());
        Assertions.assertTrue(savedAdress.isPresent(), "O endereço não foi encontrado.");
        Assertions.assertEquals(cadastroRequestDTO.adress().street(), savedAdress.get().getStreet(), "Os endereços não correspondem.");
    }

    @Test
    public void testLogin() {
        Date birthDate = java.sql.Date.valueOf(LocalDate.of(1990, 1, 1));
        CadastroRequestDTO cadastroRequestDTO = new CadastroRequestDTO(
                "12345678",
                Person_type.fisica,
                "example@email.com",
                "1234567333",
                BigDecimal.valueOf(5000.00),
                new CadastroRequestDTO.Adress(
                        "Rua Exemplo",
                        "Centro",
                        "12345678",
                        "123",
                        "SP",
                        "São Paulo",
                        "Apto 101"
                ),
                new CadastroRequestDTO.NaturalPerson(
                        "Djordan",
                        "12145678933",
                        birthDate
                ),
                null
        );
        Map<String, String> response = authService.register(cadastroRequestDTO);

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(
                Integer.valueOf(response.get("accountNumber")),
                Integer.valueOf(response.get("agencyNumber")),
                cadastroRequestDTO.password());

        ResponseLoginDTO responseLoginDTO = authService.login(loginRequestDTO);
        Assertions.assertNotNull(responseLoginDTO);
    }
}
