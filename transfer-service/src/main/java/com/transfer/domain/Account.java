package com.transfer.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;


@Entity
@Table(name = "ACCOUNT")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ACCOUNT_TYPE", discriminatorType = DiscriminatorType.STRING)
public class Account extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "CLIENT_NAME")
    private String clientName;
    
    @Column(name = "CLIENT_CODE")
    private String clientCode;
    
    public enum AccountStatus { ACCOUNT_STATUS_ACTIVE, ACCOUNT_STATUS_CLOSED };

    @Enumerated(EnumType.STRING)
    @Column(name = "ACCOUNT_STATUS")
    private AccountStatus status;
    
    public boolean equals(final Object obj) {
        if ((obj instanceof Account) && (super.getId() == ((Account) obj).getId())) {
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

    public String getClientCode() {
        return this.clientCode;
    }

    public void setClientCode(final String clientCode) {
        this.clientCode = clientCode;
    }

    public Boolean isClientAccount() {
        return (this instanceof ClientAccount);
    }

    public Boolean isLedgerAccount() {
        return (this instanceof LedgerAccount);
    }

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}
}
