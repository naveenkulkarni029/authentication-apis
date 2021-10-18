package com.kn.wallets.service;

import static org.mockito.Mockito.doReturn;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kn.wallets.domain.Transaction;
import com.kn.wallets.domain.Wallet;
import com.kn.wallets.exception.DuplicateWalletException;
import com.kn.wallets.exception.NegativeAmountException;
import com.kn.wallets.exception.TransactionIsEmptyException;
import com.kn.wallets.exception.WalletNotFoundException;
import com.kn.wallets.repository.WalletsRepository;
import com.kn.wallets.service.impl.WalletsServiceImpl;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

	@InjectMocks
	private WalletsServiceImpl walletService;

	@Mock
	private WalletsRepository walletsRepository;

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
	public void getListOfWalletsTest() {
		Mockito.when(walletsRepository.findAll()).thenReturn(responseListWallet);
		List<Wallet> actualWallets = walletService.getWallets();
		Assertions.assertEquals(responseListWallet, actualWallets);
	}

	@Test
	public void getWalletByIdSuccessTest() {
		Mockito.when(walletsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(responseWallet));
		Optional<Wallet> optionalWalletResponse = walletsRepository.findById(Mockito.anyLong());
		Assertions.assertEquals(responseWallet, optionalWalletResponse.get());
	}

	@Test
	public void getWalletByIdExceptionTest() {
		Mockito.when(walletsRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		Assertions.assertThrows(WalletNotFoundException.class, () -> walletService.getWalletById(Mockito.anyLong()));
	}

	@Test
	public void saveWalletTransactionisEmptyExceptionTest() {
		Assertions.assertThrows(TransactionIsEmptyException.class, () -> walletService.save(new Wallet()));
	}

	@Test
	public void saveWalletNegativeAmountExceptionTest() {
		Wallet wallet = new Wallet();
		List<Transaction> transactions = new ArrayList<>();
		Transaction transaction = new Transaction();
		transaction.setTransactionAmount(-10);
		transactions.add(transaction);
		wallet.setTransactions(transactions);
		Assertions.assertThrows(NegativeAmountException.class, () -> walletService.save(wallet));
	}

	@Test
	public void saveDuplicateDataExceptionTest() {
		Wallet wallet = new Wallet();
		List<Transaction> transactions = new ArrayList<>();
		Transaction transaction = new Transaction();
		transaction.setTransactionAmount(0);
		transactions.add(transaction);
		wallet.setTransactions(transactions);
		Mockito.when(walletsRepository.saveAndFlush(Mockito.any())).thenThrow(DataIntegrityViolationException.class);
		Assertions.assertThrows(DuplicateWalletException.class, () -> walletService.save(wallet));
	}

	@Test
	public void saveSuccessTest() {
		Wallet wallet = new Wallet();
		List<Transaction> transactions = new ArrayList<>();
		Transaction transaction = new Transaction();
		transaction.setTransactionAmount(0);
		transactions.add(transaction);
		wallet.setTransactions(transactions);
		Mockito.when(walletsRepository.saveAndFlush(Mockito.any())).thenReturn(responseWallet);
		Wallet walletResponse = walletService.save(wallet);
		Assertions.assertEquals(responseWallet, walletResponse);
	}
}
