package com.transfer.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "BUSINESS_TRANSACTION_DATA")
public class BusinessTransactionData implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq")
	@SequenceGenerator(name = "id_seq", sequenceName = "id_seq", allocationSize=50)
	private long id;

	@Column(name = "TRANS_DATA_KEY")
	private String key;

	@Column(name = "TRANS_DATA_VALUE")
	private String value;

	@ManyToOne(targetEntity = BusinessTransaction.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "BUSINESS_TRANSACTION_ID")
	private BusinessTransaction businessTransaction;

	public BusinessTransactionData() {
	}

	public BusinessTransactionData(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public long getId() {
		return id;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public void setKey(final String aKey) {
		key = aKey;
	}

	public void setValue(final String aValue) {
		value = aValue;
	}

	public BusinessTransaction getBusinessTransaction() {
		return businessTransaction;
	}

	public void setBusinessTransaction(final BusinessTransaction transaction) {
		businessTransaction = (BusinessTransaction) transaction;
	}

}
