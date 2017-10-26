package com.transfer.domain;

import java.math.BigDecimal;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "ACCOUNT_TRANSACTION")
@NamedQueries( {
        @NamedQuery(name = "AccountTransaction.getAll", query = "SELECT t FROM AccountTransaction t ")})
public class AccountTransaction extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "TRANSACTION_AMOUNT")
    private BigDecimal transactionAmount;

    @Column(name = "TRANSACTION_DATE")
    private Timestamp transactionDate;

    public enum TransactionStatus {
        TRANSACTION_PENDING, TRANSACTION_APPLIED
    }
    
    @Enumerated(EnumType.STRING)
    @Column(name = "TRANSACTION_STATUS")
    private TransactionStatus transactionStatus;

    @Column(name = "TRANSACTION_TYPE")
    private String transactionType;

    @ManyToOne(targetEntity = BusinessTransaction.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "BUSINESS_TRANSACTION_ID")
    private BusinessTransaction businessTransaction;

    @OneToMany(targetEntity = AccountEntry.class, mappedBy = "accountTransaction", fetch = FetchType.EAGER, cascade = {CascadeType.ALL })
    private final Set < AccountEntry > accountEntryCollection = new HashSet < AccountEntry >();

    public TransactionStatus getStatus() {
        return transactionStatus;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setStatus(final TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;

    }

    public void setTransactionDate(final Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(final String transactionType) {
        this.transactionType = transactionType;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection < AccountEntry > getAccountEntryCollection() {
        return new ArrayList(accountEntryCollection);
    }

    public AccountEntry createDebitAccountEntries(final Account anAccount) {
        return addAccountEntry(anAccount, true);
    }

    public AccountEntry createCreditAccountEntries(final Account anAccount) {
        return addAccountEntry(anAccount, false);
    }

    private AccountEntry addAccountEntry(final Account anAccount, final boolean isDebit) {
        if (anAccount == null) {
            throw new IllegalArgumentException("anAccount may not be null.");
        }
        final AccountEntry entry = new AccountEntry();
        entry.setAccountId(anAccount.getId());
        entry.setAccountTransaction(this);
        entry.setAccountEntryType(getTransactionType());
        if (isDebit) {
        	entry.addToAmount(getTransactionAmount().negate());
        } else {
            entry.addToAmount(getTransactionAmount());
        }

        entry.setAccountTransaction(this);
        accountEntryCollection.add(entry);
        return entry;
    }

    public BigDecimal getTransactionAmount() {
    	if (transactionAmount == null) {
            transactionAmount = new BigDecimal(0);
        }
        return transactionAmount;
    }

    public void addToTransactionAmount(final BigDecimal transactionAmount) {
        setTransactionAmount(getTransactionAmount().add(
                transactionAmount));
    }

    private void setTransactionAmount(final BigDecimal amount) {
        transactionAmount = amount;
    }

    public void setBusinessTransaction(final BusinessTransaction transaction) {
        businessTransaction = (BusinessTransaction) transaction;
    }

    public BusinessTransaction getBusinessTransaction() {
        return businessTransaction;
    }

    public String toString() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(this.getClass().getName());
        buffer.append(" : id - ");
        buffer.append(getId());
        buffer.append(" : transactionAmount - ");
        buffer.append(transactionAmount);
        return buffer.toString();
    }

}
