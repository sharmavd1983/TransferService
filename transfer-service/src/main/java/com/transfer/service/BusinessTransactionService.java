package com.transfer.service;

import java.math.BigDecimal;

import com.transfer.domain.Account;
import com.transfer.domain.BusinessTransaction;

public interface BusinessTransactionService {

	BusinessTransaction createBusinessTransaction(final String transactionType, final String payerClientCode, 
			final String payeeClientCode, BigDecimal amount);

	void proposeTransaction(final long payerAccountId, final long payeeAccountId, final BigDecimal amount, 
			final String transactionType, final BusinessTransaction businessTransaction);
	
	void reserveFunds(final Account debitAccount, final Account creditAccount, final BusinessTransaction businessTransaction);
	
	BusinessTransaction saveBusinessTransaction(BusinessTransaction businessTransaction);
	
	void createStatementEntry(final Account account, final BusinessTransaction businessTransaction,
            final String transactionType, final BigDecimal amount);
	
	void effectBusinessTransaction(final Account debitAccount, final Account creditAccount,
			final BusinessTransaction businessTransaction);

}
