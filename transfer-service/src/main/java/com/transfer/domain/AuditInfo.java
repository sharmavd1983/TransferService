package com.transfer.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AuditInfo implements Serializable {

	private static final long serialVersionUID = -267940718659130720L;

	@Column(name = "created_at", updatable = false)
	private Timestamp createdAt;

	@Column(name = "created_by", updatable = false)
	private String createdBy;
	
	@Column(name = "modified_at")
	private Timestamp modifiedAt;

	@Column(name = "modified_by")
	private String modifiedBy;
	
	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public Timestamp getModifiedAt() {
		return this.modifiedAt;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setCreatedAt(final Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public void setCreatedBy(final String createBy) {
		this.createdBy = createBy;
	}

	public void setModifiedAt(final Timestamp modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public void setModifiedBy(final String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	@Override
	public String toString() {
		return "AuditInfo [createdAt=" + createdAt + ", createdBy=" + createdBy
				+ ", createdByName="  + ", modifiedAt="
				+ modifiedAt + ", modifiedBy=" + modifiedBy
				+ ", modifiedByName="  + "]";
	}

}
