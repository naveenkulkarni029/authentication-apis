package com.kn.wallets.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "wallets", uniqueConstraints = @UniqueConstraint(columnNames = { "email_id" }))

public class Wallet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wallet_id")
	@ApiModelProperty(hidden = true)
	private long walletId;

	@Column(name = "wallet_name")
	private String name;

	@Column(name = "wallet_amount")
	private long amount;

	@Column(name = "email_id")
	private String emailId;

	@OneToMany(targetEntity = Transaction.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "wallet_id")
	private List<Transaction> transactions = new ArrayList<>();

	@ApiModelProperty(hidden = true)
	@Column(nullable = false, updatable = false)
	@JsonIgnore
	private String createdBy;

	@ApiModelProperty(hidden = true)
	@Column(nullable = false, updatable = false)
	@JsonIgnore
	private LocalDateTime created = LocalDateTime.now();

	@ApiModelProperty(hidden = true)
	@Column(nullable = true)
	@JsonIgnore
	private String modifiedBy;

	@ApiModelProperty(hidden = true)
	@Column(nullable = true)
	@JsonIgnore
	private LocalDateTime modified;

	public Wallet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the walletId
	 */
	public long getWalletId() {
		return walletId;
	}

	/**
	 * @param walletId the walletId to set
	 */
	public void setWalletId(long walletId) {
		this.walletId = walletId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public LocalDateTime getModified() {
		return modified;
	}

	public void setModified(LocalDateTime modified) {
		this.modified = modified;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (amount ^ (amount >>> 32));
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((emailId == null) ? 0 : emailId.hashCode());
		result = prime * result + ((modified == null) ? 0 : modified.hashCode());
		result = prime * result + ((modifiedBy == null) ? 0 : modifiedBy.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((transactions == null) ? 0 : transactions.hashCode());
		result = prime * result + (int) (walletId ^ (walletId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Wallet other = (Wallet) obj;
		if (amount != other.amount)
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (emailId == null) {
			if (other.emailId != null)
				return false;
		} else if (!emailId.equals(other.emailId))
			return false;
		if (modified == null) {
			if (other.modified != null)
				return false;
		} else if (!modified.equals(other.modified))
			return false;
		if (modifiedBy == null) {
			if (other.modifiedBy != null)
				return false;
		} else if (!modifiedBy.equals(other.modifiedBy))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (transactions == null) {
			if (other.transactions != null)
				return false;
		} else if (!transactions.equals(other.transactions))
			return false;
		if (walletId != other.walletId)
			return false;
		return true;
	}

}
