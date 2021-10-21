package com.kn.wallets.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "transactions", uniqueConstraints = @UniqueConstraint(columnNames = { "transaction_type",
		"transaction_reference_id" }))
public class Transaction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_id")
	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private long transactionId;

	@Column(name = "transaction_message", nullable = false)
	private String message;

	@Column(name = "transaction_type")
	@Enumerated(EnumType.STRING)
	private TransactionType type;

	@Column(name = "transaction_amount", nullable = false)
	private long transactionAmount;

	@ApiModelProperty(hidden = true)
	@Column(name = "transaction_reference_id", nullable = false)
	private String transactionReferenceId;

	@ApiModelProperty(hidden = true)
	@Column(nullable = false, updatable = false, name = "create_by")
	private String createdBy;

	@ApiModelProperty(hidden = true)
	@Column(nullable = false, updatable = false, name = "create_on")
	private LocalDateTime created = LocalDateTime.now();

	public Transaction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public long getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(long transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getTransactionReferenceId() {
		return transactionReferenceId;
	}

	public void setTransactionReferenceId(String transactionReferenceId) {
		this.transactionReferenceId = transactionReferenceId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + (int) (transactionAmount ^ (transactionAmount >>> 32));
		result = prime * result + (int) (transactionId ^ (transactionId >>> 32));
		result = prime * result + ((transactionReferenceId == null) ? 0 : transactionReferenceId.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Transaction other = (Transaction) obj;
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
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (transactionAmount != other.transactionAmount)
			return false;
		if (transactionId != other.transactionId)
			return false;
		if (transactionReferenceId == null) {
			if (other.transactionReferenceId != null)
				return false;
		} else if (!transactionReferenceId.equals(other.transactionReferenceId))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}
