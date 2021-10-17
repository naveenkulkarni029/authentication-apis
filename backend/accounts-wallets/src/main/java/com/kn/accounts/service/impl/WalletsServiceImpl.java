package com.kn.accounts.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kn.accounts.domain.Wallet;
import com.kn.accounts.repository.WalletsRepository;
import com.kn.accounts.service.WalletsService;

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
		Optional<Wallet> wallet = walletsRepository.findById(walletId);
		
		if(wallet.isPresent()) {
			return wallet.get();
		}
		
		return null;
		
	}

}
