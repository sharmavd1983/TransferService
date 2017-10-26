package com.transfer.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;


@Entity
@Table(name = "STATEMENT_LINE_DETAIL")
public class StatementLineDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "ACCOUNT_ID")
    private long accountId;

    @Column(name = "BUSINESS_TRANSACTION_ID")
    private long businessTransactionId;

    @Column(name = "AVAILABLE_BALANCE")
    private BigDecimal availableBalance;

    @Column(name = "CURRENT_BALANCE")
    private BigDecimal currentBalance;

    @Column(name = "TRANSACTION_AMOUNT")
    private BigDecimal transactionAmount;

    @Column(name = "DESCRIPTION_CODE")
    protected String descriptionCode;

    @Column(name = "TRANSACTION_DATE")
    private Timestamp transactionDate;

    @Column(name = "TRANSACTION_TYPE")
    private String transactionType;

    public enum StatementStatus {
        PENDING, COMPLETED
    }
    
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private StatementStatus statementStatus;

    public long getAccountId() {
        return this.accountId;
    }

    public void setAccountId(final long accountId) {
        this.accountId = accountId;
    }

    public long getBusinessTransactionId() {
        return this.businessTransactionId;
    }

    public void setBusinessTransactionId(final long businessTransactionId) {
        this.businessTransactionId = businessTransactionId;
    }

    public BigDecimal getAvailableBalance() {
        if (this.availableBalance == null) {
            this.availableBalance = new BigDecimal(0);
        }
        return this.availableBalance;
    }

    public BigDecimal getCurrentBalance() {
        if (this.currentBalance == null) {
            this.currentBalance = new BigDecimal(0);
        }
        return this.currentBalance;
    }

    public BigDecimal getTransactionAmount() {
        if (this.transactionAmount == null) {
            this.transactionAmount = new BigDecimal(0);
        }
        return this.transactionAmount;
    }

    public void addToAvailableBalance(final BigDecimal availableBalance) {
        this.setAvailableBalance(this.getAvailableBalance().add(availableBalance));
    }

    public void addToCurrentBalance(final BigDecimal currentBalance) {
        this.setCurrentBalance(this.getCurrentBalance().add(currentBalance));
    }

    public void addToTransactionAmount(final BigDecimal amount) {
        this.setTransactionAmount(this.getTransactionAmount().add(amount));
    }

    private void setAvailableBalance(final BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    private void setCurrentBalance(final BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    private void setTransactionAmount(final BigDecimal amount) {
        this.transactionAmount = amount;
    }

    public String getDescriptionCode() {
        return this.descriptionCode;
    }

    public void setDescriptionCode(final String descriptionCode) {
        this.descriptionCode = descriptionCode;
    }

    public Timestamp getTransactionDate() {
        return this.transactionDate;
    }

    public void setTransactionDate(final Timestamp transactionDate) {
        this.transactionDate = transactionDate;

    }

    public String getTransactionType() {
        return this.transactionType;
    }

    public void setTransactionType(final String transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public boolean equals(final Object obj) {
        if ((obj instanceof StatementLineDetail) && (this.getId() == ((StatementLineDetail) obj).getId())) {
            return true;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(this.getClass().getName());
        buffer.append(" : id - ");
        buffer.append(this.getId());
        return buffer.toString();
    }

    public StatementStatus getStatementStatus() {
        return this.statementStatus;
    }

    public void setStatementStatus(final StatementStatus statementStatus) {
        this.statementStatus = statementStatus;
    }
}
