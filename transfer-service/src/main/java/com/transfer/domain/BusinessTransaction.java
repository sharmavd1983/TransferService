package com.transfer.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "BUSINESS_TRANSACTION")
public class BusinessTransaction extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "BUSINESS_TRANSACTION_CODE")
	private String businessTransactionCode;

	@OneToMany(fetch = FetchType.EAGER, targetEntity = BusinessTransactionData.class, mappedBy = "businessTransaction", cascade = { CascadeType.ALL })
	private Set<BusinessTransactionData> businessTransactionDataCollection = new HashSet<BusinessTransactionData>();

	@OneToMany(fetch = FetchType.EAGER, targetEntity = ProposedTransaction.class, mappedBy = "busTrans", cascade = { CascadeType.ALL })
	private Set<ProposedTransaction> proposedTransactionCollection = new HashSet<ProposedTransaction>();

	@OneToMany(fetch = FetchType.EAGER, targetEntity = Reservation.class, mappedBy = "businessTransactionId", cascade = { CascadeType.ALL })
	private Set<Reservation> reservationCollection = new HashSet<Reservation>();

	@OneToMany(fetch = FetchType.EAGER, targetEntity = AccountTransaction.class, mappedBy = "businessTransaction", cascade = { CascadeType.ALL })
	private final Set<AccountTransaction> accountTransactionCollection = new HashSet<AccountTransaction>();

	@Column(name = "TRANSACTION_DATE")
	private Timestamp transactionDate;

	public enum BusinessTransactionStatus {
		TRANSACTION_PENDING, TRANSACTION_COMPLETED, TRANSACTION_ERROR
	};
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private BusinessTransactionStatus status;

	@Column(name = "ACCOUNT_USER_CLIENT_CODE")
	private String accountUserClientCode;

	public Collection<BusinessTransactionData> getBusinessTransactionDataCollection() {
		return this.businessTransactionDataCollection;
	}

    public Set < ProposedTransaction > getProposedTransactionCollection() {
        return this.proposedTransactionCollection;
    }

	public String getBusinessTransactionCode() {
		return this.businessTransactionCode;
	}

	public void setBusinessTransactionCode(final String code) {
		this.businessTransactionCode = code;

	}

	public BusinessTransactionStatus getStatus() {
		return this.status;
	}

	public void setStatus(final BusinessTransactionStatus status) {
		this.status = status;
	}

	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append(this.getClass().getName());
		buffer.append(" : id - ");
		buffer.append(this.getId());
		buffer.append(" : businessTransactionCode - ");
		buffer.append(this.businessTransactionCode);
		return buffer.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Set getBusinessTransactionData() {
		return new HashSet(this.businessTransactionDataCollection);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection getProposedTransactions() {
		return new ArrayList(this.proposedTransactionCollection);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setBusinessTransactionData(final Set businessTransactionData) {
		this.businessTransactionDataCollection = businessTransactionData;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setProposedTransaction(final Set proposedTransactions) {
		this.proposedTransactionCollection = proposedTransactions;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setProposedTransactionCollection(final Set proposedTransactions) {
		this.proposedTransactionCollection = proposedTransactions;
	}

	public void addProposedTransaction(final ProposedTransaction proposedTrans) {
		proposedTrans.setBusTrans(this);
		this.proposedTransactionCollection.add(proposedTrans);
	}

	public void addReservation(final Reservation reserve) {
		reserve.setBusinessTransactionId(this);
		this.reservationCollection.add(reserve);
	}

	public void removeReservation(final Reservation reserve) {
		this.reservationCollection.remove(reserve);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setReservationCollection(final Set reservedAmounts) {
		this.reservationCollection = reservedAmounts;
	}

	public Collection<Reservation> getReservedAmounts() {
		return new ArrayList<Reservation>(this.reservationCollection);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Set<Reservation> getReservationCollection() {
		return new HashSet(this.reservationCollection);
	}

	public Collection<AccountTransaction> getAccountTransactionCollection() {
		return this.accountTransactionCollection;
	}

	public void addAccountTransaction(
			final AccountTransaction accountTransaction) {
		this.accountTransactionCollection.add(accountTransaction);
	}

	public Timestamp getTransactionDate() {
		return this.transactionDate;
	}

	public void setTransactionDate(final Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}

	public void setBusinessTransactionDataCollection(
			final Set<BusinessTransactionData> businessTransactionDataCollection) {
		this.businessTransactionDataCollection = businessTransactionDataCollection;
	}

    public void addBusinessTransactionData(
            final BusinessTransactionData businessTransactionData) {
        this.businessTransactionDataCollection.add(businessTransactionData);
    }

	public void addBusinessTransactionData(final String key, final String value) {
		final BusinessTransactionData businessTransactionData = new BusinessTransactionData();
		businessTransactionData.setKey(key);
		businessTransactionData.setValue(value);
		businessTransactionData.setBusinessTransaction(this);
		this.addBusinessTransactionData(businessTransactionData);
	}

	public BusinessTransactionData getBusinessTransactionData(final String key) {
		for (final BusinessTransactionData businessTransactionData : this.businessTransactionDataCollection) {
			if (businessTransactionData.getKey().equalsIgnoreCase(key)) {
				return businessTransactionData;
			}
		}
		return null;
	}

	public String getAccountUserClientCode() {
		return this.accountUserClientCode;
	}

	public void setAccountUserClientCode(final String clientCode) {
		this.accountUserClientCode = clientCode;

	}

	public long getAccountToBeCredited() {
		long accountToBeCredited = 0;
		for (final ProposedTransaction proposedTransaction : this.proposedTransactionCollection) {
			accountToBeCredited = proposedTransaction
					.getAccountToBeCredited();
			break;
		}
		return accountToBeCredited;
	}

}
