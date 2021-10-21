package com.kn.wallets.controller.advice;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kn.wallets.exception.DuplicateWalletException;
import com.kn.wallets.exception.FieldNotAllowedException;
import com.kn.wallets.exception.NegativeAmountException;
import com.kn.wallets.exception.SameWalletTransferException;
import com.kn.wallets.exception.WalletNotFoundException;
import com.kn.wallets.exception.ZeroAmountTransferException;

@RestControllerAdvice
public class ServiceControllerAdvice {
	private static final Logger log = LoggerFactory.getLogger(ServiceControllerAdvice.class);

	@ExceptionHandler(value = { DuplicateWalletException.class })
	public ResponseEntity<Object> handleDuplicateWalletException(DuplicateWalletException exception) {
		log.info(exception.getLocalizedMessage(), exception);
		return ResponseEntity.badRequest().body(createMessage(exception.getLocalizedMessage()));
	}
	
	@ExceptionHandler(value = { FieldNotAllowedException.class })
	public ResponseEntity<Object> handleFieldNotAllowedException(FieldNotAllowedException exception) {
		log.info(exception.getLocalizedMessage(), exception);
		return ResponseEntity.badRequest().body(createMessage(exception.getLocalizedMessage()));
	}

	@ExceptionHandler(value = { WalletNotFoundException.class })
	public ResponseEntity<Object> handleWalletNotFoundException(WalletNotFoundException exception) {
		log.info(exception.getLocalizedMessage(), exception);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createMessage(exception.getLocalizedMessage()));
	}

	@ExceptionHandler(value = { NegativeAmountException.class })
	public ResponseEntity<Object> handleNegativeAmountException(NegativeAmountException exception) {
		log.info(exception.getLocalizedMessage(), exception);
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(createMessage(exception.getLocalizedMessage()));
	}

	@ExceptionHandler(value = { SameWalletTransferException.class })
	public ResponseEntity<Object> handleSameWalletTransferException(SameWalletTransferException exception) {
		log.info(exception.getLocalizedMessage(), exception);
		return ResponseEntity.badRequest().body(createMessage(exception.getLocalizedMessage()));
	}

	@ExceptionHandler(value = { ZeroAmountTransferException.class })
	public ResponseEntity<Object> handleZeroAmountTransferException(ZeroAmountTransferException exception) {
		log.info(exception.getLocalizedMessage(), exception);
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(createMessage(exception.getLocalizedMessage()));
	}

	private Map<String, String> createMessage(String exceptionMessage) {
		Map<String, String> map = new HashMap<>();
		map.put("message", exceptionMessage);
		return map;
	}

}
