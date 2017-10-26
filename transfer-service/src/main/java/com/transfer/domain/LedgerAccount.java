package com.transfer.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue(value = "L")
public class LedgerAccount extends Account {

    private static final long serialVersionUID = 1L;

    @Column(name = "LEDGER_ACCOUNT_TYPE")
    private String ledgerAccountType;

    @Column(name = "CURRENT_BALANCE")
    private BigDecimal currentBalance;
    
    @Column(name = "RESERVED_AMOUNT")
    private BigDecimal reservedAmount;
    
    public void setLedgerAccountType(final String aLedgerAccountType) {
        this.ledgerAccountType = aLedgerAccountType;
    }

    public String getLedgerAccountType() {
        return this.ledgerAccountType;
    }

    public boolean isControlAccount() {
        return "CONTROL".equals(this.ledgerAccountType);
    }

}
