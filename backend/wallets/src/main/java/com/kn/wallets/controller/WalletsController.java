package com.kn.wallets.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kn.wallets.domain.Transaction;
import com.kn.wallets.domain.Wallet;
import com.kn.wallets.service.WalletsService;

@RestController
@RequestMapping("/api/v1")
public class WalletsController {

	private static final Logger log = LoggerFactory.getLogger(WalletsController.class);
	private WalletsService walletsService;

	@Autowired
	public WalletsController(WalletsService walletsService) {
		this.walletsService = walletsService;
	}

	@PostMapping(value = "/wallets", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Wallet> save(@RequestBody Wallet wallet) {
		log.info("requested to save wallet: " + wallet);
		return ResponseEntity.status(201).body(walletsService.save(wallet));
	}

	@GetMapping(value = "/wallets")
	public ResponseEntity<List<Wallet>> getWallets() {
		log.info("requested to retrieve wallets");
		return ResponseEntity.ok(walletsService.getWallets());
	}

	@GetMapping(value = "/wallets/{walletId}")
	public ResponseEntity<Wallet> getWallet(@PathVariable long walletId) {
		log.info("requested to retrieve wallet: Id: " + walletId);
		return ResponseEntity.ok(walletsService.getWalletById(walletId));
	}

	@PostMapping(value = "/wallets/{walletId}")
	public ResponseEntity<Wallet> updateWallet(@PathVariable long walletId, @RequestBody Transaction transaction) {
		log.info("requested to update wallet: Id: " + walletId + " with Transaction: " + transaction);
		return ResponseEntity.ok(walletsService.updateWallet(walletId, transaction));
	}

	@PostMapping(value = "/wallets/{walletId}/transfer/{targetWalletId}")
	public ResponseEntity<List<Wallet>> transferToWallet(@PathVariable long walletId, @PathVariable long targetWalletId,
			@RequestBody Transaction transaction) {
		log.info("requested to transfer wallet from : Id: " + walletId + " to: " + targetWalletId
				+ " with Transaction: " + transaction);
		return ResponseEntity.ok(walletsService.transferToWallet(walletId, targetWalletId, transaction));

	}

}
