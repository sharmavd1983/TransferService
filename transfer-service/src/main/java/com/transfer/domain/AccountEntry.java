package com.transfer.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "ACCOUNT_ENTRY")
public class AccountEntry extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(targetEntity = AccountTransaction.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "ACCOUNT_TRANSACTION_ID", nullable = false, updatable = false)
    private AccountTransaction accountTransaction;

    @Column(name = "ACCOUNT_ENTRY_TYPE")
    private String accountEntryType;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "ACCOUNT_ID")
    private long accountId;

    public AccountTransaction getAccountTransaction() {
        return accountTransaction;
    }

    public void setAccountTransaction(final AccountTransaction transaction) {
        accountTransaction = (AccountTransaction) transaction;
    }

    public String getAccountEntryType() {
        return accountEntryType;
    }

    public void setAccountEntryType(final String accountEntryType) {
        this.accountEntryType = accountEntryType;
    }

    public void addToAmount(final BigDecimal anAmount) {
        setAmount(getAmount().add(anAmount));
    }

    public BigDecimal getAmount() {
        if (amount == null) {
            amount = new BigDecimal(0);
        }
        return amount;
    }

    private void setAmount(final BigDecimal anAmount) {
        amount = anAmount;
    }

    public boolean equals(final Object obj) {
        if ((obj instanceof AccountEntry)
                && (getId() == ((AccountEntry) obj).getId())) {
            return true;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(this.getClass().getName());
        buffer.append(" : id - ");
        buffer.append(getId());
        return buffer.toString();
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(final long accountId) {
        this.accountId = accountId;
    }
}
