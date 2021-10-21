package com.kn.wallets.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.kn.wallets.constants.WalletsConstants;
import com.kn.wallets.domain.Transaction;
import com.kn.wallets.domain.TransactionType;
import com.kn.wallets.domain.Wallet;
import com.kn.wallets.exception.DuplicateWalletException;
import com.kn.wallets.exception.FieldNotAllowedException;
import com.kn.wallets.exception.NegativeAmountException;
import com.kn.wallets.exception.SameWalletTransferException;
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
				.orElseThrow(() -> new WalletNotFoundException(WalletsConstants.WALLET_NOT_FOUND_MESSAGE));
		log.info("Service fetched wallet retrieve wallet: Id: " + walletId + " is " + wallet);
		Comparator<Transaction> reverseComparator = (t1, t2) -> {
			return (t2.getCreated()).compareTo(t1.getCreated());
		};
		Collections.sort(wallet.getTransactions(), reverseComparator);
		return wallet;
	}

	@Override
	public Wallet save(Wallet wallet) {
		log.info("Service called to save wallet: " + wallet);
		try {
			if (wallet.getAmount() < 0) {
				log.info(WalletsConstants.NEGATIVE_AMOUNT_MESSAGE);
				throw new NegativeAmountException(WalletsConstants.NEGATIVE_AMOUNT_MESSAGE);
			}
			if(!StringUtils.hasText(wallet.getEmailId())) {
				throw new FieldNotAllowedException(WalletsConstants.EMAIL_NOT_BLANK_MESSAGE);
			}
			
			if(!StringUtils.hasText(wallet.getName())) {
				throw new FieldNotAllowedException(WalletsConstants.NAME_NOT_BLANK_MESSAGE);
			}
			if (wallet.getAmount() > 0) {
				List<Transaction> transactions = new ArrayList<>();
				Transaction transaction = new Transaction();
				TransactionType transactionType = TransactionType.CREDIT;
				transaction.setMessage(WalletsConstants.INITIAL_TRANSACTION);
				transaction.setType(transactionType);
				String txRefId = getTxRefId(WalletsConstants.SELF, transactionType);
				transaction.setTransactionReferenceId(txRefId);
				transaction.setCreatedBy(WalletsConstants.SELF);
				transaction.setTransactionAmount(wallet.getAmount());
				transactions.add(transaction);
				wallet.setTransactions(transactions);
			}
			wallet.setCreatedBy(wallet.getEmailId());
			log.info("wallet object before calling the repository: " + wallet);
			return walletsRepository.saveAndFlush(wallet);
		} catch (DataIntegrityViolationException ex) {
			log.info(WalletsConstants.EMAIL_ID_EXIST_MESSAGE, ex);
			throw new DuplicateWalletException(WalletsConstants.EMAIL_ID_EXIST_MESSAGE);
		}
	}

	@Override
	public Wallet updateWallet(long walletId, Transaction transaction) {
		if (transaction.getTransactionAmount() == 0) {
			log.info(WalletsConstants.AMOUNT_ZERO_MESSAGE);
			throw new ZeroAmountTransferException(WalletsConstants.AMOUNT_ZERO_MESSAGE);
		}

		if (transaction.getTransactionAmount() < 0) {
			log.info(WalletsConstants.NEGATIVE_AMOUNT_MESSAGE);
			throw new NegativeAmountException(WalletsConstants.NEGATIVE_AMOUNT_MESSAGE);
		}
		Wallet wallet = getWalletById(walletId);
		long walletAmount = wallet.getAmount();
		long transactionAmount = transaction.getTransactionAmount();
		if (TransactionType.DEBIT.equals(transaction.getType())) {
			if (transactionAmount <= walletAmount) {
				wallet.setAmount(subtractAmount(walletAmount, transactionAmount));
			} else {
				log.info(WalletsConstants.AVAILABLE_LESS_THAN_DEBIT_MESSAGE);
				throw new NegativeAmountException(WalletsConstants.AVAILABLE_LESS_THAN_DEBIT_MESSAGE);
			}
		} else if (TransactionType.CREDIT.equals(transaction.getType())) {
			wallet.setAmount(addAmount(walletAmount, transactionAmount));
		}
		String txRefId = getTxRefId(WalletsConstants.SELF, transaction.getType());
		transaction.setTransactionReferenceId(txRefId);
		transaction.setCreatedBy(WalletsConstants.SELF);
		wallet.getTransactions().add(transaction);
		wallet.setModified(LocalDateTime.now());
		wallet.setModifiedBy(WalletsConstants.SELF);
		Wallet responseWallet = walletsRepository.saveAndFlush(wallet);
		Comparator<Transaction> reverseComparator = (t1, t2) -> {
			return (t2.getCreated()).compareTo(t1.getCreated());
		};
		Collections.sort(responseWallet.getTransactions(), reverseComparator);
		return responseWallet;
	}

	@Override
	public List<Wallet> transferToWallet(long walletId, long targetWalletId, Transaction transaction) {
		if (walletId == targetWalletId) {
			log.info(WalletsConstants.SAME_WALLET_MESSAGE);
			throw new SameWalletTransferException(WalletsConstants.SAME_WALLET_MESSAGE);
		}
		if (transaction.getTransactionAmount() == 0) {
			log.info(WalletsConstants.AMOUNT_ZERO_MESSAGE);
			throw new ZeroAmountTransferException(WalletsConstants.AMOUNT_ZERO_MESSAGE);
		}

		if (transaction.getTransactionAmount() < 0) {
			log.info(WalletsConstants.NEGATIVE_AMOUNT_MESSAGE);
			throw new NegativeAmountException(WalletsConstants.NEGATIVE_AMOUNT_MESSAGE);
		}

		Wallet sourceWallet = getWalletById(walletId);

		long sourceWalletAmount = sourceWallet.getAmount();
		long transactionAmount = transaction.getTransactionAmount();

		String uuid = UUID.randomUUID().toString();

		List<Wallet> wallets = new ArrayList<>();

		if (transactionAmount <= sourceWalletAmount) {
			Wallet targetWallet = getWalletById(targetWalletId);

			sourceWallet.setAmount(subtractAmount(sourceWalletAmount, transactionAmount));
			sourceWallet.setModified(LocalDateTime.now());
			sourceWallet.setModifiedBy(WalletsConstants.SELF);

			Transaction sourceTransaction = new Transaction();

			sourceTransaction.setType(TransactionType.DEBIT);
			sourceTransaction.setCreatedBy(WalletsConstants.SELF);
			sourceTransaction.setMessage(transaction.getMessage());
			sourceTransaction.setTransactionAmount(transaction.getTransactionAmount());

			sourceTransaction.setTransactionReferenceId(
					uuid + "/" + targetWallet.getEmailId() + "/" + sourceTransaction.getType());
			sourceWallet.getTransactions().add(sourceTransaction);

			long targetWalletAmount = targetWallet.getAmount();

			targetWallet.setAmount(addAmount(targetWalletAmount, transactionAmount));
			targetWallet.setModified(LocalDateTime.now());
			targetWallet.setModifiedBy(sourceWallet.getName() + "\n( " + sourceWallet.getEmailId() + " )");

			Transaction targetTransaction = new Transaction();
			targetTransaction.setType(TransactionType.CREDIT);

			targetTransaction.setCreatedBy(sourceWallet.getName() + "( " + sourceWallet.getEmailId() + " )");

			targetTransaction.setMessage(transaction.getMessage());
			targetTransaction.setTransactionAmount(transaction.getTransactionAmount());

			targetTransaction.setTransactionReferenceId(
					uuid + "/" + sourceWallet.getEmailId() + "/" + targetTransaction.getType());
			targetWallet.getTransactions().add(targetTransaction);
			wallets.add(sourceWallet);
			wallets.add(targetWallet);
		} else {
			log.info(WalletsConstants.AVAILABLE_LESS_THAN_DEBIT_MESSAGE);
			throw new NegativeAmountException(WalletsConstants.AVAILABLE_LESS_THAN_DEBIT_MESSAGE);
		}
		return walletsRepository.saveAllAndFlush(wallets);
	}

	private String getTxRefId(String createdBy, TransactionType type) {
		StringBuilder builder = new StringBuilder(UUID.randomUUID().toString());
		builder.append("/").append(createdBy).append("/").append(type);
		return builder.toString();
	}

	private long addAmount(long currentAmount, long transactionAmount) {
		return currentAmount + transactionAmount;
	}

	private long subtractAmount(long currentAmount, long transactionAmount) {
		return currentAmount - transactionAmount;
	}

}
