package com.backend.banking_backend.service;

import java.math.BigDecimal;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

import com.backend.banking_backend.model.User;
import com.backend.banking_backend.dto.AccountResponse;
import com.backend.banking_backend.dto.UserResponse;
import com.backend.banking_backend.model.Account;
import com.backend.banking_backend.repository.UserRepository;
import com.backend.banking_backend.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse registerUser(User user) {

        if (userRepository.findByCpf(user.getCpf()).isPresent()) {
            throw new RuntimeException("CPF já cadastrado");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        User savedUser = userRepository.save(user);
        
       createAccount(user);
        
        return new UserResponse(
        	savedUser.getId(),
        	savedUser.getEmail(),
        	savedUser.getPassword(),
        	savedUser.getName(),
        	savedUser.getCpf()
        );
    }

    private AccountResponse createAccount(User user) {

        Account account = new Account();
        account.setBankCode("001");
        account.setAgency("0001");
        account.setBalance(BigDecimal.ZERO);
        
        String accountNumber = generateUniqueAccountNumber();
        account.setAccountNumber(accountNumber);
        account.setCheckDigit(generateCheckDigit(accountNumber));

        account.setUser(user);
        
        Account savedAccount = accountRepository.save(account);

        return new AccountResponse(
        	savedAccount.getId(),
        	savedAccount.getBankCode(),
        	savedAccount.getAgency(),
        	savedAccount.getAccountNumber(),
        	savedAccount.getCheckDigit(),
        	savedAccount.getBalance()
        );
    }

    private String generateUniqueAccountNumber() {

        String number;

        do {
            number = String.format("%08d", new Random().nextInt(100000000));
        } while (accountRepository.findByAccountNumber(number).isPresent());

        return number;
    }

    private String generateCheckDigit(String accountNumber) {

        int sum = 0;
        int multiplier = 2;

        for (int i = accountNumber.length() - 1; i >= 0; i--) {
            int num = Character.getNumericValue(accountNumber.charAt(i));
            sum += num * multiplier;
            multiplier = (multiplier == 9) ? 2 : multiplier + 1;
        }

        int remainder = sum % 11;
        int digit = 11 - remainder;

        if (digit >= 10) {
            digit = 0;
        }

        return String.valueOf(digit);
    }

    public boolean passwordMatches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
