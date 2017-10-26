package com.transfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.transfer.domain.StatementLineDetail;

public interface StatementLineDetailRepository extends JpaRepository<StatementLineDetail, Long> {

	
}
