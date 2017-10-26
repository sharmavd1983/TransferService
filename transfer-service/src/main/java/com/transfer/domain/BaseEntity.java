package com.transfer.domain;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.transfer.audit.AuditInfoAwareListener;

@MappedSuperclass
@EntityListeners({ AuditInfoAwareListener.class })
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = -5966523326237353836L;

	@Embedded
	@Access(AccessType.FIELD)
	private AuditInfo auditInfo;

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq")
	@SequenceGenerator(name = "id_seq", sequenceName = "id_seq", allocationSize=100)
	private Long id;
	
	@Version
	@Column(name = "version")
	private long version;

	protected BaseEntity() {
	}

	protected BaseEntity(final Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final BaseEntity other = (BaseEntity) obj;

		if (this.getId() == null && other.getId() == null) {
			return false;
		}
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}

	public AuditInfo getAuditInfo() {
		return this.auditInfo;
	}

	public Long getId() {
		return this.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.id == null ? 0 : this.id.hashCode());
		return result;
	}

	@JsonIgnore
	public boolean isNew() {
		return this.id == null;
	}

	public void setAuditInfo(final AuditInfo auditInfo) {
		this.auditInfo = auditInfo;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + "(id:" + this.id + ")";
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}
}
