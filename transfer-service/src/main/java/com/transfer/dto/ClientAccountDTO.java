package com.transfer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ClientAccountDTO implements Serializable {

	private static final long serialVersionUID = -2587568387840757657L;

	private String clientName;
	private String clientCode;
	private BigDecimal initialBalance;
	
	public String getClientName() {
		return clientName;
	}
	
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	public String getClientCode() {
		return clientCode;
	}
	
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	
	public BigDecimal getInitialBalance() {
		return initialBalance;
	}
	
	public void setInitialBalance(BigDecimal initialBalance) {
		this.initialBalance = initialBalance;
	}

	@Override
	public String toString() {
		return "ClientAccountDTO [clientName=" + clientName + ", clientCode=" + clientCode + ", initialBalance="
				+ initialBalance + "]";
	}
}
