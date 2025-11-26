package com.backend.newbank.AccountTransaction.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.newbank.AccountTransaction.DTO.AuthDTOS.CadastroRequestDTO;
import com.backend.newbank.AccountTransaction.DTO.AuthDTOS.LoginRequestDTO;
import com.backend.newbank.AccountTransaction.DTO.AuthDTOS.ResponseLoginDTO;
import com.backend.newbank.AccountTransaction.entity.*;
import com.backend.newbank.AccountTransaction.entity.Enum.Person_type;
import com.backend.newbank.AccountTransaction.repository.*;
import com.backend.newbank.exception.WrongPasswordException;
import com.backend.newbank.securityConfig.TokenJWTService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private NaturalPersonRepository naturalPersonRepository;

    @Autowired
    private LegalEntityRespository legalEntityRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AdressRepository adressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenJWTService tokenJWTService;

    @Transactional
    public ResponseLoginDTO login(LoginRequestDTO body) {
        Account account = accountRepository.findByAccount_NumberAndAgency_Number(body.accountNumber(), body.agencyNumber())
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        if(!account.getIsActive()) {
            throw new EntityNotFoundException("Account not found");
        }

        if (!passwordEncoder.matches(body.password(), account.getUser().getPassword())) {
            throw new WrongPasswordException("Wrong password");
        }

        String username = getUsername(account);
        String accountNumberAndAgency = body.accountNumber() + "-" + body.agencyNumber();
        String email = account.getUser().getPerson().getEmail();

        return new ResponseLoginDTO(
                username,
                accountNumberAndAgency,
                email,
                account.getId(),
                tokenJWTService.generateToken(account.getId())
        );
    }

    @Transactional
    public Map<String, String> register(CadastroRequestDTO body) {
        Map<String, String> response = new HashMap<>();

        // Verifica se o CPF ou CNPJ já está registrado e retorna mensagem apropriada
        if (body.personType() == Person_type.fisica) {
            Optional<Natural_Person> existingNaturalPerson = naturalPersonRepository.findByCpf(body.naturalPerson().cpf());
            if (existingNaturalPerson.isPresent()) {
                return handleExistingAccount(existingNaturalPerson.get().getPerson().getId(), body.naturalPerson().cpf(), "CPF");
            }
        } else if (body.personType() == Person_type.juridica) {
            Optional<Legal_Entity> existingLegalEntity = legalEntityRepository.findByCnpj(body.legalEntity().cnpj());
            if (existingLegalEntity.isPresent()) {
                return handleExistingAccount(existingLegalEntity.get().getPerson().getId(), body.legalEntity().cnpj(), "CNPJ");
            }
        }

        if(body.password().length() < 8) {
            response.put("message", "A senha deve ter pelo menos 8 caracteres");
            return response;
        }

        Person person = createPerson(body);
        createSpecificPerson(body, person);
        createAddress(body, person);
        User user = createUser(body, person);
        Account account = createAccount(user);

        response.put("message", "Usuário criado com sucesso!");
        response.put("accountNumber", String.valueOf(account.getAccount_Number()));
        response.put("agencyNumber", String.valueOf(account.getAgency_Number()));

        return response;
    }

    private String getUsername(Account account) {
        if (account.getUser().getPerson().getPerson_Type() == Person_type.fisica) {
            return naturalPersonRepository.findByPersonId(account.getUser().getPerson().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Natural person not found"))
                    .getFull_Name();
        } else if (account.getUser().getPerson().getPerson_Type() == Person_type.juridica) {
            return legalEntityRepository.findByPersonId(account.getUser().getPerson().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Legal entity not found"))
                    .getRazao_Social();
        }
        throw new IllegalArgumentException("Tipo de pessoa inválido");
    }

    private Map<String, String> handleExistingAccount(Long personId, String identifier, String type) {
        Optional<Account> accountOptional = accountRepository.findByUser_Id(userRepository.findByPerson_id(personId).orElseThrow().getId());
        Map<String, String> response = new HashMap<>();

        if (accountOptional.isPresent() && accountOptional.get().getIsActive()) {
            response.put("message", "Usuário já existe com o " + type + ": " + identifier);
        } else if (accountOptional.isPresent()) {
            accountOptional.get().setIsActive(true);
            accountRepository.save(accountOptional.get());
            response.put("message", "Conta sendo reativada para o " + type + ": " + identifier);
        }

        return response;
    }

    private Person createPerson(CadastroRequestDTO body) {
        Person person = new Person();
        person.setPerson_Type(body.personType());
        person.setEmail(body.email());
        person.setPhone_Number(body.phone_number());
        person.setMonthly_Income(body.monthly_income()); // Já salva a renda mensal
        personRepository.save(person);
        return person;
    }

    private void createSpecificPerson(CadastroRequestDTO body, Person person) {
        if (body.personType() == Person_type.fisica) {
            Natural_Person naturalPerson = new Natural_Person();
            naturalPerson.setFull_Name(body.naturalPerson().full_name());
            naturalPerson.setCpf(body.naturalPerson().cpf());
            naturalPerson.setBorn_Date(body.naturalPerson().born_date());
            naturalPerson.setPerson(person);
            naturalPersonRepository.save(naturalPerson);
        } else if (body.personType() == Person_type.juridica) {
            Legal_Entity legalEntity = new Legal_Entity();
            legalEntity.setRazao_Social(body.legalEntity().razao_social());
            legalEntity.setCnpj(body.legalEntity().cnpj());
            legalEntity.setResponsible(body.legalEntity().responsible());
            legalEntity.setPerson(person);
            legalEntityRepository.save(legalEntity);
        }
    }

    private void createAddress(CadastroRequestDTO body, Person person) {
        Adress adress = new Adress();
        adress.setStreet(body.adress().street());
        adress.setNeighborhood(body.adress().neighborhood());
        adress.setCep(body.adress().cep());
        adress.setNumber(body.adress().number());
        adress.setState(body.adress().state());
        adress.setCity(body.adress().city());
        adress.setComplement(body.adress().complement());
        adress.setPerson(person);
        adressRepository.save(adress);
    }

    private User createUser(CadastroRequestDTO body, Person person) {
        User user = new User();
        user.setPassword(passwordEncoder.encode(body.password()));
        user.setPerson(person);
        userRepository.save(user);
        return user;
    }

    private Account createAccount(User user) {
        Random random = new Random();
        
        // Gera o número da agência diretamente entre 1 e 10
        int agencyNumber = random.nextInt(10) + 1;
        
        // Gera um número de conta único, se necessário
        int accountNumber = generateUniqueAccountNumber(random, 100); // Limite de 100 tentativas
        
        Account account = new Account();
        account.setUser(user); // Agora associa o User já salvo
        account.setAccount_Number(accountNumber);
        account.setAgency_Number(agencyNumber);
        account.setBalance(BigDecimal.valueOf(0.00));
        accountRepository.save(account);
        return account;
    }
    
    private int generateUniqueAccountNumber(Random random, int maxAttempts) {
    int accountNumber;
    int attempts = 0;
    do {
        if (attempts >= maxAttempts) {
            throw new IllegalStateException("Falha ao gerar um número de conta único após " + maxAttempts + " tentativas.");
        }
        accountNumber = random.nextInt(9000) + 1000;
        attempts++;
    } while (accountRepository.existsByAccountNumber(accountNumber));
    return accountNumber;
}
    

    public boolean confirmAuthentication(LoginRequestDTO body) {
        Long accountId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        return passwordEncoder.matches(body.password(), account.getUser().getPassword());
    }
}
