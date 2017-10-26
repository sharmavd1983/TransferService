package com.transfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.transfer.domain.BusinessTransaction;

public interface BusinessTransactionRepository extends JpaRepository<BusinessTransaction, Long> {

	
}
