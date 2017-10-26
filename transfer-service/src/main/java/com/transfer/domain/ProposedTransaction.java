package com.transfer.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.transfer.domain.BusinessTransaction.BusinessTransactionStatus;

@Entity
@Table(name = "PROPOSED_TRANSACTION")
public class ProposedTransaction extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(targetEntity = BusinessTransaction.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "BUSINESS_TRANSACTION_ID")
    private BusinessTransaction busTrans;

    @Column(name = "ACCOUNT_TRANSACTION_TYPE")
    private String accountTransactionType;

    @Column(name = "ACCOUNT_TO_DEBIT")
    private long accountToBeDebited;

    @Column(name = "ACCOUNT_TO_CREDIT")
    private long accountToBeCredited;

    @Column(name = "TRANSACTION_DATE")
    private Timestamp transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private BusinessTransaction.BusinessTransactionStatus status;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    public long getAccountToBeCredited() {
        return accountToBeCredited;
    }

    public long getAccountToBeDebited() {
        return accountToBeDebited;
    }

    public String getAccountTransactionType() {
        return accountTransactionType;
    }

    public void setAccountTransactionType(final String accTransType) {
        accountTransactionType = accTransType;
    }

    public void addToAmount(final BigDecimal amount) {
        setAmount(getAmount().add(amount));
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public void setAccountToBeDebited(final long accToBeDebited) {
        accountToBeDebited = accToBeDebited;
    }

    public BusinessTransaction getBusTrans() {
        return busTrans;
    }

    public void setBusTrans(final BusinessTransaction busTrans) {
        this.busTrans = (BusinessTransaction) busTrans;
    }

    public void setAccountToBeCredited(final long accToBeCredited) {
        accountToBeCredited = accToBeCredited;

    }

    @Override
    public String toString() {
        final StringBuffer string = new StringBuffer();
        string.append("Proposed Transaction id:" + getId() + " Credit User acc:"
                + accountToBeCredited + " Debit User acc:"
                + accountToBeDebited);
        System.out.println("string:::" + string);
        return string.toString();

    }

    public BusinessTransactionStatus getStatus() {
        return status;
    }

    public void setStatus(final BusinessTransactionStatus aStatus) {
        status = aStatus;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(final Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

}
