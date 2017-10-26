package com.transfer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ClientTransferDTO implements Serializable {

	private static final long serialVersionUID = -2587568387840757657L;

	private String payerClientCode;
	private String payeeClientCode;
	private BigDecimal amount;
	
	public String getPayerClientCode() {
		return payerClientCode;
	}
	
	public void setPayerClientCode(String payerClientCode) {
		this.payerClientCode = payerClientCode;
	}
	
	public String getPayeeClientCode() {
		return payeeClientCode;
	}
	
	public void setPayeeClientCode(String payeeClientCode) {
		this.payeeClientCode = payeeClientCode;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "ClientTransferDTO [payerClientCode=" + payerClientCode + ", payeeClientCode=" + payeeClientCode
				+ ", amount=" + amount + "]";
	}
}
