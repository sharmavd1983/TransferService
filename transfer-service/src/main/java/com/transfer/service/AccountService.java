package com.transfer.service;

import java.math.BigDecimal;

import com.transfer.domain.Account;
import com.transfer.domain.ClientAccount;
import com.transfer.dto.ClientAccountDTO;

public interface AccountService {
	
	Account createClientAccount(final ClientAccountDTO accountDTO);
	
	Account updateAccount(Account account);
	
	BigDecimal getAvailableBalance(final ClientAccount account);

	Account resolveClientAccount(String clientCode);

}
