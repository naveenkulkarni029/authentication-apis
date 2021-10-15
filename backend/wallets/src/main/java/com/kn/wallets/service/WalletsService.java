package com.kn.wallets.service;

import java.util.List;

import com.kn.wallets.domain.Wallet;

public interface WalletsService {

	List<Wallet> getWallets();

	Wallet getWalletById(long walletId);

}
