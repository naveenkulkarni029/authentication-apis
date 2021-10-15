package com.kn.wallets.service.impl;

import org.springframework.stereotype.Service;

import com.kn.wallets.repository.WalletsRepository;
import com.kn.wallets.service.WalletsService;

@Service
public class WalletsServiceImpl implements WalletsService {

	private WalletsRepository walletsRepository;

	public WalletsServiceImpl(WalletsRepository walletsRepository) {
		this.walletsRepository = walletsRepository;
	}

}
