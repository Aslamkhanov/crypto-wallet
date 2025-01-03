package com.javaacademy.cryptowallet.mapper;

import com.javaacademy.cryptowallet.crypto.CryptoAccount;
import com.javaacademy.cryptowallet.dto.CryptoAccountDto;
import org.springframework.stereotype.Component;

@Component
public class CryptoAccountMapper {
    public CryptoAccountDto convertCryptoAccountDto(CryptoAccount account) {
        return new CryptoAccountDto(account.getLogin(),
                account.getCryptoCurrencyType(),
                account.getBalance(),
                account.getUniqueAccountNumber());
    }
}
