package com.transfer.audit;

import java.sql.Timestamp;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.transfer.domain.AuditInfo;
import com.transfer.domain.BaseEntity;

public class AuditInfoAwareListener {

	private static Logger logger = LoggerFactory.getLogger(AuditInfoAwareListener.class);
	public static final String USER_ID = "SYSTEM";

	@PrePersist
	public void beforeSave(final BaseEntity entity) {
		logger.trace("AuditInfoAwareListener.beforeSave invoked for entity {} and id {}  ", entity.getClass().getName(), entity.getId());
		final AuditInfo auditInfo = entity.getAuditInfo() == null ? new AuditInfo() : entity.getAuditInfo();
		if (auditInfo.getCreatedAt() == null) {
			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			auditInfo.setCreatedBy(USER_ID);
			auditInfo.setCreatedAt(currentTime);
			auditInfo.setModifiedAt(currentTime);
			auditInfo.setModifiedBy(USER_ID);
		}
		logger.trace("AuditInfoAwareListener.beforeSave invoked for entity {}, id {}  and  auditInfo {} ", entity.getClass().getName(), entity.getId(),
		        auditInfo.toString());
		entity.setAuditInfo(auditInfo);
	}

	/**
	 * Sets audit information of the entity at the time of entity update operation.
	 * @param entity The entity being modified.
	 */
	@PreUpdate
	public void beforeUpdate(final BaseEntity entity) {
		logger.trace("AuditInfoAwareListener.beforeUpdate invoked for entity {}, id {} ", entity.getClass().getName(), entity.getId());
		final AuditInfo auditInfo = entity.getAuditInfo() == null ? new AuditInfo() : entity.getAuditInfo();
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		auditInfo.setModifiedBy(USER_ID);
		auditInfo.setModifiedAt(currentTime);
		logger.trace("AuditInfoAwareListener.beforeUpdate invoked for entity {}, id {} and  auditInfo {} ", entity.getClass().getName(), entity.getId(),
		        auditInfo.toString());
		entity.setAuditInfo(auditInfo);
	}
}
