package com.kn.wallets.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kn.wallets.domain.Wallet;
import com.kn.wallets.service.WalletsService;

@ExtendWith(MockitoExtension.class)
public class WalletsControllerTest {

	@InjectMocks
	private WalletsController walletsController;

	@Mock
	private WalletsService walletService;

	private ObjectMapper objectMapper;

	private Wallet responseWallet;

	private List<Wallet> responseListWallet;

	private List<Wallet> transferWalletResponse;

	@BeforeEach
	public void setup() throws JsonParseException, JsonMappingException, IOException {
		MockitoAnnotations.openMocks(this);
		objectMapper = new ObjectMapper();

		File walletResponseFile = new File("src/test/resources/wallet-response.json");
		Assertions.assertTrue(walletResponseFile.exists());
		responseWallet = objectMapper.readValue(walletResponseFile, Wallet.class);

		File transferWalletResponseFile = new File("src/test/resources/transfer-wallet-response.json");
		Assertions.assertTrue(transferWalletResponseFile.exists());

		transferWalletResponse = Arrays.asList(objectMapper.readValue(transferWalletResponseFile, Wallet[].class));

		responseListWallet = new ArrayList<>();
		responseListWallet.add(responseWallet);
	}

	@Test
	public void SaveWalletTestSuccess() {
		Mockito.when(walletService.save(Mockito.any())).thenReturn(responseWallet);
		ResponseEntity<Wallet> actualResponse = walletsController.save(Mockito.any());
		Assertions.assertEquals(responseWallet, actualResponse.getBody());
	}

	@Test
	public void getWalletListTestSuccess() {
		Mockito.when(walletService.getWallets()).thenReturn(responseListWallet);
		ResponseEntity<List<Wallet>> actualListWalletResponse = walletsController.getWallets();
		Assertions.assertEquals(responseListWallet, actualListWalletResponse.getBody());
	}

	@Test
	public void getWalletTestSuccess() {
		Mockito.when(walletService.getWalletById(Mockito.anyLong())).thenReturn(responseWallet);
		ResponseEntity<Wallet> actualWalletResponse = walletsController.getWallet(Mockito.anyLong());
		Assertions.assertEquals(responseWallet, actualWalletResponse.getBody());
	}

	@Test
	public void updateWalletTestSuccess() {
		Mockito.when(walletService.updateWallet(Mockito.anyLong(), Mockito.any())).thenReturn(responseWallet);
		ResponseEntity<Wallet> actualWalletResponse = walletsController.updateWallet(Mockito.anyLong(), Mockito.any());
		Assertions.assertEquals(responseWallet, actualWalletResponse.getBody());
	}

	@Test
	public void TransferWalletAmountTestSuccess() {
		Mockito.when(walletService.transferToWallet(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
				.thenReturn(transferWalletResponse);
		ResponseEntity<List<Wallet>> actualListWalletResponse = walletsController.transferToWallet(Mockito.anyLong(),
				Mockito.anyLong(), Mockito.any());
		Assertions.assertEquals(transferWalletResponse, actualListWalletResponse.getBody());
	}
}
