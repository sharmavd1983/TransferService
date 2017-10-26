package com.transfer.domain;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "RESERVATION")
public class Reservation extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(targetEntity = BusinessTransaction.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "BUSINESS_TRANS_ID")
    private BusinessTransaction businessTransactionId;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.EAGER, cascade = {CascadeType.ALL })
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @Column(name = "INITIAL_RESERVATION")
    private BigDecimal initialReservationAmount;

    @Column(name = "RESERVED_AMOUNT")
    private BigDecimal reservedAmount;

    public Account getAccount() {
        return account;
    }

    public void setAccount(final Account account) {
        this.account = (Account) account;
    }

    public BigDecimal getInitialReservationAmount() {
        if (initialReservationAmount == null) {
            initialReservationAmount = new BigDecimal(0);
        }
        return initialReservationAmount;
    }

    public void addToInitialReservationAmount(final BigDecimal amount) {
        setInitialReservationAmount(getInitialReservationAmount()
                .add(amount));
    }

    public BigDecimal getReservedAmount() {
        if (reservedAmount == null) {
            reservedAmount = new BigDecimal(0);
        }
        return reservedAmount;
    }

    public void addToReservedAmount(final BigDecimal amount) {
        setReservedAmount(getReservedAmount().add(amount));
    }

    private void setInitialReservationAmount(final BigDecimal amount) {
        initialReservationAmount = amount;
    }

    public void zeroReservedAmount() {
        reservedAmount = new BigDecimal(0);
    }

    private void setReservedAmount(final BigDecimal reservedAmount) {
        this.reservedAmount = reservedAmount;
    }

    public void setBusinessTransactionId(
            final BusinessTransaction businessTransactionId) {
        this.businessTransactionId = (BusinessTransaction) businessTransactionId;
    }

    public BusinessTransaction getBusinessTransactionId() {
        return businessTransactionId;
    }

}
