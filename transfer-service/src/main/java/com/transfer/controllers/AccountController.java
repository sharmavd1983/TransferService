package com.transfer.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.transfer.dto.ClientAccountDTO;
import com.transfer.dto.ClientTransferDTO;
import com.transfer.service.AccountService;
import com.transfer.service.TransferService;

@RestController
@RequestMapping("accounts")
public class AccountController {
	
	Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private TransferService transferService; 
	
	
	@RequestMapping(value="/create", produces="application/json", method = RequestMethod.POST)
	public String createClientAccount(@RequestBody ClientAccountDTO accountDTO ) {
		
		LOGGER.debug("Create client account :: {}  ", accountDTO);
		accountService.createClientAccount(accountDTO);
		
		return "SUCCESS";
	}
	
	@RequestMapping(value="/transfer", produces="application/json", method = RequestMethod.POST)
	public String transferMoney(@RequestBody ClientTransferDTO clientTransferDTO ) {
		
		LOGGER.debug("Transfer money within client accounts :: {}  ", clientTransferDTO);
		transferService.transferMoney(clientTransferDTO);
		
		return "SUCCESS";
	}
}
