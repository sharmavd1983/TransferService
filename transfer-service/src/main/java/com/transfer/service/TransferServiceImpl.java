package com.transfer.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.transfer.constants.TransferConstants;
import com.transfer.domain.Account;
import com.transfer.domain.Account.AccountStatus;
import com.transfer.domain.BusinessTransaction;
import com.transfer.domain.ClientAccount;
import com.transfer.domain.Reservation;
import com.transfer.dto.ClientTransferDTO;
import com.transfer.exception.ValidationException;

@Component
public class TransferServiceImpl implements TransferService {

	Logger LOGGER = LoggerFactory.getLogger(TransferService.class);
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private BusinessTransactionService businessTransactionService;
	
	@Transactional
	public void transferMoney(final ClientTransferDTO clientTransferDTO) {
		
		LOGGER.info("Transfer money from one account to another account.");
		
		if(null == clientTransferDTO) {
			LOGGER.info("Invalid request. Request cannot ne null.");
			throw new ValidationException("Invalid request. Request cannot ne null.");
		}
		
		validateClientTransfer(clientTransferDTO);
		
		Account payerClientAccount = accountService.resolveClientAccount(clientTransferDTO.getPayerClientCode());
		
		if(null == payerClientAccount) {
			LOGGER.info("Invalid payer client code. Please provide valid payer client code.");
			throw new ValidationException("Invalid payer client code. Please provide valid payer client code.");
		}
		
		Account payeeClientAccount = accountService.resolveClientAccount(clientTransferDTO.getPayeeClientCode());
		
		if(null == payeeClientAccount) {
			LOGGER.info("Invalid payee client code. Please provide valid payee client code.");
			throw new ValidationException("Invalid payee client code. Please provide valid payee client code.");
		}
		
		List<Account.AccountStatus> listAllowedStatusFrom = new ArrayList<Account.AccountStatus>();
		listAllowedStatusFrom.add(Account.AccountStatus.ACCOUNT_STATUS_ACTIVE);
		
		// Validate account status before initiating transfer
		validateClientAccountsForTransfer(payerClientAccount, payeeClientAccount, listAllowedStatusFrom);
		
		// Validate available balance with payer account
		validateClientAccountBalance(payerClientAccount, clientTransferDTO.getAmount());
		
		// Create business transaction
		BusinessTransaction businessTransaction = businessTransactionService.createBusinessTransaction(TransferConstants.TRANSFER, payerClientAccount.getClientCode(), 
				payeeClientAccount.getClientCode(), clientTransferDTO.getAmount());
		
		// Propose transaction before reserving money and moving money
		businessTransactionService.proposeTransaction(payerClientAccount.getId(), payeeClientAccount.getId(), clientTransferDTO.getAmount(), 
				TransferConstants.TRANSFER, businessTransaction);
		
		// Reserve funds before actual money movement
		businessTransactionService.reserveFunds(payerClientAccount, payeeClientAccount, businessTransaction);
		
		for (final Reservation reservation : businessTransaction.getReservationCollection()) {
			accountService.updateAccount(reservation.getAccount());
        }
		
		businessTransaction = businessTransactionService.saveBusinessTransaction(businessTransaction);
		
		// create statement line entries for both payer and payee accounts
		businessTransactionService.createStatementEntry(payerClientAccount, businessTransaction, TransferConstants.TRANSFER, clientTransferDTO.getAmount());
		businessTransactionService.createStatementEntry(payeeClientAccount, businessTransaction, TransferConstants.TRANSFER, clientTransferDTO.getAmount());
		
		// effect the transaction which will actually move money from one account to another and complete the transaction
		businessTransactionService.effectBusinessTransaction(payerClientAccount, payeeClientAccount, businessTransaction);
	}

	private void validateClientTransfer(final ClientTransferDTO clientTransferDTO) {
		
		LOGGER.info("Validate client account transfer request.");
		
		if(StringUtils.isBlank(clientTransferDTO.getPayerClientCode())) {
			LOGGER.info("Payer client code cannot be blank. Please provide valid payer client code.");
			throw new ValidationException("Payer client code cannot be blank. Please provide valid payer client code.");
		}
		
		if(StringUtils.isBlank(clientTransferDTO.getPayeeClientCode())) {
			LOGGER.info("Payee client code cannot be blank. Please provide valid payee client code.");
			throw new ValidationException("Payee client code cannot be blank. Please provide valid payee client code.");
		}
		
		if(null == clientTransferDTO.getAmount() || clientTransferDTO.getAmount().longValue() <= 0) {
			LOGGER.info("Transfer amount cannot be blank, zero or negative. Please provide valid transfer amount.");
			throw new ValidationException("Transfer amount cannot be blank, zero or negative. Please provide valid transfer amount.");
		}
	}
	
	private void validateClientAccountsForTransfer(Account payerClientAccount, Account payeeClientAccount,
			List<AccountStatus> allowedAccountStatuses) {
		
		if (payerClientAccount.isLedgerAccount()) {
			LOGGER.info("Invalid payer client code. Please provide valid payer client code.");
			throw new ValidationException("Invalid payer client code. Please provide valid payer client code.");
        }
		
		if (payeeClientAccount.isLedgerAccount()) {
			LOGGER.info("Invalid payee client code. Please provide valid payee client code.");
			throw new ValidationException("Invalid payee client code. Please provide valid payee client code.");
        }

        // Check valid account statuses
        if ((allowedAccountStatuses != null) && (allowedAccountStatuses.size() > 0)) {
            boolean isPayerAccountValid = false;
            boolean isPayeeAccountValid = false;
            for (final AccountStatus status : allowedAccountStatuses) {
                if (status.toString().equalsIgnoreCase(payerClientAccount.getStatus().toString())) {
                    isPayerAccountValid = true;
                }
                if (status.toString().equalsIgnoreCase(payeeClientAccount.getStatus().toString())) {
                    isPayeeAccountValid = true;
                }
            }
            if (!isPayerAccountValid || !isPayeeAccountValid) {
            	LOGGER.info("Invalid payer or payee account status.");
    			throw new ValidationException("Invalid payer or payee account status.");
            }
        }
	}
	
	private void validateClientAccountBalance(Account payerClientAccount, BigDecimal amount) {
		
		BigDecimal leftBalance = ((ClientAccount)payerClientAccount).getCurrentBalance().subtract(amount);
		if (BigDecimal.ZERO.compareTo(leftBalance) > 0) {
			LOGGER.info("Un-sufficient funds in payer account. Please provide valid amount for transfer.");
			throw new ValidationException("Un-sufficient funds in payer account. Please provide valid amount for transfer.");
		}
	}
	
	
}
