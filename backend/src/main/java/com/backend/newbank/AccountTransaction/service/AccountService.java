package com.backend.newbank.AccountTransaction.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.newbank.AccountTransaction.DTO.AccountDTO.BalanceDetailsDTO;
import com.backend.newbank.AccountTransaction.DTO.AccountDTO.UpdateAccountDTO;
import com.backend.newbank.AccountTransaction.entity.*;
import com.backend.newbank.AccountTransaction.entity.Enum.Person_type;
import com.backend.newbank.AccountTransaction.repository.*;
import com.backend.newbank.Payment.entity.PixKey;
import com.backend.newbank.Payment.repository.PixKeyRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    private NaturalPersonRepository naturalPersonRepository;

    @Autowired
    private LegalEntityRespository legalEntityRespository;

    @Autowired
    private AdressRepository addressRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PixKeyRepository pixKeyRepository; // Repositório para as chaves PIX


    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public BalanceDetailsDTO getAccountBalance(Long accountId) {
        // Buscar a conta pelo ID da conta
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada para o ID da conta: " + accountId));

        // Retornar apenas o saldo da conta
        return new BalanceDetailsDTO(account.getBalance());
    }

        // NOVO: Método para listar as chaves PIX associadas ao usuário autenticado
    public List<PixKey> getPixKeysByAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        // Retorna as chaves PIX associadas ao usuário
        return pixKeyRepository.findAllByUserId(account.getUser().getId());
    }

    // NOVO: Método para cadastrar uma nova chave PIX associada à conta
    public boolean registerPixKey(Long accountId, String pixKey) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        // Verifica se a chave PIX já está cadastrada
        boolean exists = pixKeyRepository.findByKey(pixKey).isPresent();
        if (exists) {
            throw new IllegalArgumentException("Chave PIX já cadastrada.");
        }

        // Cadastra a nova chave PIX associada ao usuário da conta
        PixKey newPixKey = new PixKey();
        newPixKey.setKey(pixKey);
        newPixKey.setUserId(account.getUser().getId());
        pixKeyRepository.save(newPixKey);

        return true;
    }

    public BigDecimal deposit(Long accountId, BigDecimal amount, String boletoCode) {
        // Buscar a conta pelo ID da conta
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada para o ID da conta: " + accountId));

        // Atualizar o saldo da conta
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);

        // Salvar a conta atualizada no repositório
        accountRepository.save(account);

        // Retornar o novo saldo
        return newBalance;
    }

    public String findFullNameByAccountAndAgency(Integer accountNumber, Integer agencyNumber) {
        Account account = accountRepository.findByAccount_NumberAndAgency_Number(accountNumber, agencyNumber)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        Person person = account.getUser().getPerson(); // Obtém a pessoa associada à conta

        // Verifica o tipo de pessoa e busca o nome completo conforme o tipo
        if (person.getPerson_Type() == Person_type.fisica) {
            Natural_Person naturalPerson = naturalPersonRepository.findByPersonId(person.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Pessoa física não encontrada"));
            return naturalPerson.getFull_Name();
        } else if (person.getPerson_Type() == Person_type.juridica) {
            Legal_Entity legalEntity = legalEntityRespository.findByPersonId(person.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Pessoa jurídica não encontrada"));
            return legalEntity.getRazao_Social();
        } else {
            return "Tipo de pessoa desconhecido";
        }
    }

    public boolean updateAccountDetails(Long accountId, UpdateAccountDTO updateAccountDTO) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada para o ID da conta: " + accountId));

        Person person = account.getUser().getPerson();
        User user = account.getUser();

        // Atualizar o endereço
        if (updateAccountDTO.address() != null) {
            updateAddress(person.getId(), updateAccountDTO.address());
        }

        // Atualizar e-mail
        if (updateAccountDTO.email() != null) {
            person.setEmail(updateAccountDTO.email());
        }

        // Atualizar senha (criptografada) no User
        if (updateAccountDTO.password() != null) {
            user.setPassword(passwordEncoder.encode(updateAccountDTO.password()));
        }

        if (updateAccountDTO.monthly_Income() != null) {
            person.setMonthly_Income(updateAccountDTO.monthly_Income());
        }

        if (updateAccountDTO.phoneNumber() != null) {
            person.setPhone_Number(updateAccountDTO.phoneNumber());
        }

        // Atualizar nome conforme o tipo de pessoa
        updatePersonName(person, updateAccountDTO);

        personRepository.save(person);
        userRepository.save(user); // Salvar as alterações feitas no User
        return true;
    }

    // Método auxiliar para atualizar o endereço usando o repositório diretamente
    private void updateAddress(Long personId, UpdateAccountDTO.Adress addressDTO) {
        Adress address = addressRepository.findByPerson_id(personId)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado para a pessoa com ID: " + personId));

        address.setStreet(addressDTO.street());
        address.setNeighborhood(addressDTO.neighborhood());
        address.setCep(addressDTO.cep());
        address.setNumber(addressDTO.number());
        address.setState(addressDTO.state());
        address.setCity(addressDTO.city());
        address.setComplement(addressDTO.complement());
        addressRepository.save(address);
    }

    // Método auxiliar para atualizar o nome com base no tipo de pessoa (física ou jurídica)
    private void updatePersonName(Person person, UpdateAccountDTO updateAccountDTO) {
        if (updateAccountDTO.naturalPerson() != null && person.getPerson_Type() == Person_type.fisica) {
            Natural_Person naturalPerson = naturalPersonRepository.findByPersonId(person.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Pessoa física não encontrada"));
            naturalPerson.setFull_Name(updateAccountDTO.naturalPerson().fullName());
            naturalPersonRepository.save(naturalPerson);
        } else if (updateAccountDTO.legalEntity() != null && person.getPerson_Type() == Person_type.juridica) {
            Legal_Entity legalEntity = legalEntityRespository.findByPersonId(person.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Pessoa jurídica não encontrada"));
            legalEntity.setRazao_Social(updateAccountDTO.legalEntity().razaoSocial());
            legalEntityRespository.save(legalEntity);
        }
    }

    public UpdateAccountDTO getAccountDetails(Long accountId) {
        // Buscar a conta pelo ID
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada para o ID da conta: " + accountId));

        Person person = account.getUser().getPerson();

        // Construir o DTO de endereço
        Adress address = addressRepository.findByPerson_id(person.getId())
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado para a pessoa com ID: " + person.getId()));

        UpdateAccountDTO.Adress addressDTO = new UpdateAccountDTO.Adress(
                address.getStreet(),
                address.getNeighborhood(),
                address.getCep(),
                address.getNumber(),
                address.getState(),
                address.getCity(),
                address.getComplement()
        );

        // Construir o DTO de pessoa física, se aplicável
        UpdateAccountDTO.NaturalPerson naturalPersonDTO = null;
        if (person.getPerson_Type() == Person_type.fisica) {
            Natural_Person naturalPerson = naturalPersonRepository.findByPersonId(person.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Pessoa física não encontrada"));
            naturalPersonDTO = new UpdateAccountDTO.NaturalPerson(
                    naturalPerson.getFull_Name(),
                    naturalPerson.getCpf(),
                    naturalPerson.getBorn_Date()
            );
        }

        // Construir o DTO de pessoa jurídica, se aplicável
        UpdateAccountDTO.LegalEntity legalEntityDTO = null;
        if (person.getPerson_Type() == Person_type.juridica) {
            Legal_Entity legalEntity = legalEntityRespository.findByPersonId(person.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Pessoa jurídica não encontrada"));
            legalEntityDTO = new UpdateAccountDTO.LegalEntity(
                    legalEntity.getRazao_Social(),
                    legalEntity.getCnpj(),
                    legalEntity.getResponsible()
            );
        }

        // Construir o DTO principal
        return new UpdateAccountDTO(
                null, // Senha não é retornada por questões de segurança
                person.getPerson_Type(),
                person.getEmail(),
                person.getMonthly_Income(),
                person.getPhone_Number(),
                addressDTO,
                naturalPersonDTO,
                legalEntityDTO,
                account.getAccount_Number().toString()
        );
    }

    @Transactional
    public void deleteAccount(Long accountId) {
        accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Conta nao encontrada"));

        accountRepository.closeAccount(accountId);

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(null);
    }


}
