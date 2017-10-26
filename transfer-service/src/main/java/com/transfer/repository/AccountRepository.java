package com.transfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.transfer.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

	Long countByClientCode(String clientCode);
	
	Account findByClientCode(String clientCode);
}
