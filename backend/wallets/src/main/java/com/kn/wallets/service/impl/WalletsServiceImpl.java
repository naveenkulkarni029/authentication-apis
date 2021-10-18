package com.kn.wallets.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.kn.wallets.domain.Transaction;
import com.kn.wallets.domain.TransactionType;
import com.kn.wallets.domain.Wallet;
import com.kn.wallets.exception.DuplicateWalletException;
import com.kn.wallets.exception.NegativeAmountException;
import com.kn.wallets.exception.SameWalletTransferException;
import com.kn.wallets.exception.TransactionIsEmptyException;
import com.kn.wallets.exception.WalletNotFoundException;
import com.kn.wallets.exception.ZeroAmountTransferException;
import com.kn.wallets.repository.WalletsRepository;
import com.kn.wallets.service.WalletsService;

@Service
@Transactional
public class WalletsServiceImpl implements WalletsService {

	private static final Logger log = LoggerFactory.getLogger(WalletsServiceImpl.class);

	private WalletsRepository walletsRepository;

	@Autowired
	public WalletsServiceImpl(WalletsRepository walletsRepository) {
		this.walletsRepository = walletsRepository;
	}

	@Override
	public List<Wallet> getWallets() {
		log.info("Service called to retrieve list wallets");
		return walletsRepository.findAll();
	}

	@Override
	public Wallet getWalletById(long walletId) {
		log.info("Service called to retrieve wallet: Id: " + walletId);
		Optional<Wallet> walletOptional = walletsRepository.findById(walletId);
		Wallet wallet = walletOptional
				.orElseThrow(() -> new WalletNotFoundException("walletId " + walletId + " not found"));
		log.info("Service fetched wallet retrieve wallet: Id: " + walletId + " is " + wallet);
		return wallet;
	}

	@Override
	public Wallet save(Wallet wallet) {
		log.info("Service called to save wallet: " + wallet);
		try {
			if (!CollectionUtils.isEmpty(wallet.getTransactions())) {
				if (wallet.getTransactions().get(0).getTransactionAmount() < 0) {
					log.info("Negative Amount not allowed while creating a wallet");
					throw new NegativeAmountException("Negative Amount not allowed while creating a wallet");
				}
				List<Transaction> transactions = new ArrayList<>();
				Transaction transaction = wallet.getTransactions().get(0);
				transaction.setName("first Transaction");
				transaction.setType(TransactionType.CREDIT);
				transaction.setTransactionReferenceId(UUID.randomUUID().toString() + "/SELF/" + TransactionType.CREDIT);
				transaction.setCreatedBy("SELF");
				transactions.add(transaction);
				wallet.setAmount(transaction.getTransactionAmount());
				wallet.setTransactions(transactions);
				wallet.setCreatedBy(wallet.getEmailId());
				log.info("wallet object before calling the repository: " + wallet);
				return walletsRepository.saveAndFlush(wallet);
			} else {
				log.info("Transaction Cannot Be Empty");
				throw new TransactionIsEmptyException("Transaction Cannot Be Empty");
			}
		} catch (DataIntegrityViolationException ex) {
			log.info("email id already exists", ex);
			throw new DuplicateWalletException("Email id already Exists");
		}
	}

	@Override
	public Wallet updateWallet(long walletId, Transaction transaction) {
		if (transaction.getTransactionAmount() == 0) {
			log.info("Cannot transfer Zero Amount");
			throw new ZeroAmountTransferException("Cannot transfer Zero Amount");
		}

		if (transaction.getTransactionAmount() < 0) {
			log.info("Negative Amount not allowed");
			throw new NegativeAmountException("Negative Amount not allowed");
		}
		Wallet wallet = getWalletById(walletId);
		if (TransactionType.DEBIT.equals(transaction.getType())) {
			long walletAmount = wallet.getAmount();
			long transactionAmount = transaction.getTransactionAmount();
			if (transactionAmount <= walletAmount) {
				wallet.setAmount(walletAmount - transactionAmount);
			} else {
				log.info("Debit amoumt Requested on Wallet is greater than Available amount in wallet ");
				throw new NegativeAmountException(
						"Debit amoumt Requested on Wallet is greater than Available amount in wallet ");
			}
		}
		if (TransactionType.CREDIT.equals(transaction.getType())) {
			wallet.setAmount(wallet.getAmount() + transaction.getTransactionAmount());
		}
		transaction.setTransactionReferenceId(UUID.randomUUID().toString() + "/SELF/" + transaction.getType());
		transaction.setCreatedBy("SELF");
		wallet.getTransactions().add(transaction);
		wallet.setModified(LocalDateTime.now());
		wallet.setModifiedBy("SELF");
		return walletsRepository.saveAndFlush(wallet);
	}

	@Override
	public List<Wallet> transferToWallet(long walletId, long targetWalletId, Transaction transaction) {
		if (walletId == targetWalletId) {
			log.info("Same wallet Transfer");
			throw new SameWalletTransferException("Same wallet Transfer");
		}
		if (transaction.getTransactionAmount() == 0) {
			log.info("Cannot transfer Zero Amount");
			throw new ZeroAmountTransferException("Cannot transfer Zero Amount");
		}

		if (transaction.getTransactionAmount() < 0) {
			log.info("Negative Amount not allowed");
			throw new NegativeAmountException("Negative Amount not allowed");
		}

		Wallet sourceWallet = getWalletById(walletId);
		long sourceWalletAmount = sourceWallet.getAmount();
		long transactionAmount = transaction.getTransactionAmount();
		String uuid = UUID.randomUUID().toString();
		List<Wallet> wallets = new ArrayList<>();
		if (transactionAmount <= sourceWalletAmount) {
			Wallet targetWallet = getWalletById(targetWalletId);

			sourceWallet.setAmount(sourceWalletAmount - transactionAmount);
			sourceWallet.setModified(LocalDateTime.now());
			sourceWallet.setModifiedBy("SELF");

			Transaction sourceTransaction = new Transaction();

			sourceTransaction.setType(TransactionType.DEBIT);
			sourceTransaction.setCreatedBy("SELF");
			sourceTransaction.setName(transaction.getName());
			sourceTransaction.setTransactionAmount(transaction.getTransactionAmount());

			sourceTransaction.setTransactionReferenceId(
					uuid + "/" + targetWallet.getEmailId() + "/" + sourceTransaction.getType());
			sourceWallet.getTransactions().add(sourceTransaction);

			long targetWalletAmount = targetWallet.getAmount();
			targetWallet.setAmount(targetWalletAmount + transactionAmount);
			Transaction targetTransaction = new Transaction();
			targetTransaction.setType(TransactionType.CREDIT);

			targetTransaction.setCreatedBy(sourceWallet.getEmailId());

			targetTransaction.setName(transaction.getName());
			targetTransaction.setTransactionAmount(transaction.getTransactionAmount());

			targetTransaction.setTransactionReferenceId(
					uuid + "/" + sourceWallet.getEmailId() + "/" + targetTransaction.getType());
			targetWallet.getTransactions().add(targetTransaction);
			wallets.add(sourceWallet);
			wallets.add(targetWallet);
		} else {
			log.info("Debit amoumt Requested on Wallet is greater than Available amount in wallet ");
			throw new NegativeAmountException(
					"Debit amoumt Requested on Wallet is greater than Available amount in wallet ");
		}
		return walletsRepository.saveAllAndFlush(wallets);
	}

}
