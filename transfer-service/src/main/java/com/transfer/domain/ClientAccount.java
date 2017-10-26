package com.transfer.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue(value = "C")
public class ClientAccount extends Account {

    private static final long serialVersionUID = 1L;

    @Column(name = "CURRENT_BALANCE")
    private BigDecimal currentBalance;

    @Column(name = "RESERVED_AMOUNT")
    private BigDecimal reservedAmount;

    public ClientAccount() {
    }

    public ClientAccount(String clientCode, String clientName, BigDecimal initialBalance) {
		super.setClientCode(clientCode);
		super.setClientName(clientName);
		super.setStatus(AccountStatus.ACCOUNT_STATUS_ACTIVE);
		this.currentBalance = initialBalance;
		this.reservedAmount = new BigDecimal(0);
	}

    public BigDecimal getReservedAmount() {
    	if (reservedAmount == null) {
            reservedAmount = new BigDecimal(0);
        }
        return this.reservedAmount;
    }

    public void setReservedAmount(final BigDecimal reservedAmount) {
        this.reservedAmount = reservedAmount;
    }

    public void addToReservedAmount(final BigDecimal amount) {
        this.setReservedAmount(this.getReservedAmount().add(amount));
    }

    public void subtractToReservedAmount(final BigDecimal amount) {
        this.setReservedAmount(this.getReservedAmount().subtract(amount));
    }

    public BigDecimal getCurrentBalance() {
    	if (currentBalance == null) {
            currentBalance = new BigDecimal(0);
        }
        return this.currentBalance;
    }

    public void addToCurrentBalance(final BigDecimal amount) {
        this.setCurrentBalance(this.getCurrentBalance().add(amount));
    }

    public void subtractToCurrentBalance(final BigDecimal amount) {
        this.setCurrentBalance(this.getCurrentBalance().subtract(amount));
    }

    public void setCurrentBalance(final BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }
}
