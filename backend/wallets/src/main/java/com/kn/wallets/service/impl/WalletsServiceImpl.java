package com.kn.wallets.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kn.wallets.domain.Wallet;
import com.kn.wallets.repository.WalletsRepository;
import com.kn.wallets.service.WalletsService;

@Service
public class WalletsServiceImpl implements WalletsService {

	private WalletsRepository walletsRepository;

	public WalletsServiceImpl(WalletsRepository walletsRepository) {
		this.walletsRepository = walletsRepository;
	}

	@Override
	public List<Wallet> getWallets() {
		return walletsRepository.findAll();
	}

	@Override
	public Wallet getWalletById(long walletId) {
		return walletsRepository.findById(walletId).orElseThrow();
	}

}
