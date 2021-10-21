package com.kn.wallets.constants;

public class WalletsConstants {
	
	public static final String INITIAL_TRANSACTION ="Initial Transaction";
	public static final String SELF = "SELF";
	
	
	// Exception Messages
	public static final String NEGATIVE_AMOUNT_MESSAGE="Negative Amount not allowed";
	public static final String EMAIL_ID_EXIST_MESSAGE="Email id already Exists";
	public static final String AMOUNT_ZERO_MESSAGE="Cannot transfer Zero Amount";
	public static final String AVAILABLE_LESS_THAN_DEBIT_MESSAGE="Debit amount Requested on Wallet is greater than Available amount in wallet ";
	public static final String SAME_WALLET_MESSAGE="Same Wallet Transfer not allowed";
	public static final String WALLET_NOT_FOUND_MESSAGE = "Wallet Not Found";
	public static final String EMAIL_NOT_BLANK_MESSAGE="Email id cannot be blank";
	public static final String NAME_NOT_BLANK_MESSAGE="Name cannot be blank";
	
	private WalletsConstants() {}
	

}
