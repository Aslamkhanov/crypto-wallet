package com.javaacademy.cryptowallet.storage;

import com.javaacademy.cryptowallet.entity.CryptoAccount;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Data
public class CryptoAccountStorage {
    private final Map<UUID, CryptoAccount> cryptoData = new HashMap<>();
}
