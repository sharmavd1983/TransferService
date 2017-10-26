package com.transfer.service;

import com.transfer.dto.ClientTransferDTO;

public interface TransferService {
	
	void transferMoney(final ClientTransferDTO clientTransferDTO);

}
