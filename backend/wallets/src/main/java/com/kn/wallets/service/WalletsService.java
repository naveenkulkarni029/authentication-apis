package com.kn.wallets.service;

import java.util.List;

import com.kn.wallets.domain.Transaction;
import com.kn.wallets.domain.Wallet;

public interface WalletsService {

	List<Wallet> getWallets();

	Wallet getWalletById(long walletId);

	Wallet save(Wallet wallet);

	Wallet updateWallet(long walletId, Transaction transaction);

	List<Wallet> transferToWallet(long walletId, long targetWalletId, Transaction transaction);

}
