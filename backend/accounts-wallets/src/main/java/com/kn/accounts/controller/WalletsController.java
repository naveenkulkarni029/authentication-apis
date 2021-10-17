package com.kn.accounts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kn.accounts.domain.Wallet;
import com.kn.accounts.service.WalletsService;

@RestController
@RequestMapping("/v1")
public class WalletsController {

	private WalletsService walletsService;

	@Autowired
	public WalletsController(WalletsService walletsService) {
		this.walletsService = walletsService;
	}

	@GetMapping(value = "/wallets")
	public ResponseEntity<List<Wallet>> getWallets() {
		return ResponseEntity.ok(walletsService.getWallets());
	}

	@GetMapping(value = "/wallets/{walletId}")
	public ResponseEntity<Wallet> getWallet(@PathVariable long walletId) {
		return ResponseEntity.ok(walletsService.getWalletById(walletId));
	}

}
