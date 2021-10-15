package com.kn.wallets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.kn.wallets.service.WalletsService;

@RestController
public class WalletsController {
	
	private WalletsService walletsService;
	
	@Autowired
	public WalletsController(WalletsService walletsService) {
		this.walletsService = walletsService;
	}

}
