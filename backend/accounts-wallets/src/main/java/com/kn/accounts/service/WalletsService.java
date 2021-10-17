package com.kn.accounts.service;

import java.util.List;

import com.kn.accounts.domain.Wallet;

public interface WalletsService {

	List<Wallet> getWallets();

	Wallet getWalletById(long walletId);

}
