package com.transfer.service;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.transfer.domain.Account;
import com.transfer.domain.ClientAccount;
import com.transfer.dto.ClientAccountDTO;
import com.transfer.exception.ValidationException;
import com.transfer.repository.AccountRepository;

@Component
public class AccountServiceImpl implements AccountService {

	Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Transactional
	public Account createClientAccount(final ClientAccountDTO accountDTO) {
		
		LOGGER.info("Create client account.");
		
		if(null == accountDTO) {
			LOGGER.info("Invalid request. Request cannot ne null.");
			throw new ValidationException("Invalid request. Request cannot ne null.");
		}
		
		validateClientAccount(accountDTO);
		
		Account account = populateAccount(accountDTO);
		
		account = accountRepository.save(account);
		
		return account;
	}

	private void validateClientAccount(final ClientAccountDTO accountDTO) {
		
		LOGGER.info("Validate client account request");
		
		if(StringUtils.isBlank(accountDTO.getClientCode())) {
			LOGGER.info("Client code cannot be blank. Please provide valid client code.");
			throw new ValidationException("Client code cannot be blank. Please provide valid client code.");
		}
		
		if(StringUtils.isBlank(accountDTO.getClientName())) {
			LOGGER.info("Client name cannot be blank. Please provide valid client name.");
			throw new ValidationException("Client name cannot be blank. Please provide valid client name.");
		}
		
		if(null == accountDTO.getInitialBalance() || accountDTO.getInitialBalance().longValue() < 0) {
			LOGGER.info("Balance cannot be blank or negative. Please provide valid initial balance.");
			throw new ValidationException("Balance cannot be blank or negative. Please provide valid initial balance.");
		}
		
		boolean isClientCodeExists = validateDuplicateAccountByClientCode(accountDTO.getClientCode());
		
		if(isClientCodeExists) {
			LOGGER.info("Client code already exists. Client code should ne uinque.");
			throw new ValidationException("Client code already exists. Client code should ne uinque.");
		}
	}
	
	private boolean validateDuplicateAccountByClientCode(final String clientCode) {
		Long count = accountRepository.countByClientCode(clientCode);
		return count != null && count > 0;
	}

	private Account populateAccount(final ClientAccountDTO accountDTO) {
		Account account = new ClientAccount(accountDTO.getClientCode(), accountDTO.getClientName(), accountDTO.getInitialBalance());
		return account;
	}
	
	@Transactional
	public Account updateAccount(Account account) {
		account = accountRepository.save(account);
		return account;
	}
	
	 public BigDecimal getAvailableBalance(final ClientAccount account) {
        final BigDecimal result = account.getCurrentBalance().subtract(
                account.getReservedAmount());
        return result;
    }

	public Account resolveClientAccount(String clientCode) {
		return accountRepository.findByClientCode(clientCode);
	}
}
