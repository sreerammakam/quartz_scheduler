package com.lca.eps.common.persistence.entity;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "PRPRTY_VAL", uniqueConstraints = {@UniqueConstraint(columnNames = "PRPRTY_VAL_ID")})
public class PropertyValueEntity {

	@Id
	@GeneratedValue
	@JdbcTypeCode(SqlTypes.CHAR)
	@Column(name = "PRPRTY_VAL_ID", unique = true)
	private UUID id;

	@Column(name = "PRPRTY_NAME")
	private String propertyName;

	@Lob
	@Column(name = "PRPRTY_VAL")
	private String propertyValue;

	@Column(name = "CREAT_USER_ID")
	private String userCreated;

	@Column(name = "CREAT_TS")
	private Date dateCreated;

	@Column(name = "LAST_CHG_USER_ID")
	private String userUpdated;

	@Column(name = "LAST_CHG_TS")
	private Date dateUpdated;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public String getUserCreated() {
		return userCreated;
	}

	public void setUserCreated(String userCreated) {
		this.userCreated = userCreated;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getUserUpdated() {
		return userUpdated;
	}

	public void setUserUpdated(String userUpdated) {
		this.userUpdated = userUpdated;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	@Override
	public String toString() {
		return "Job [id=" + id + ", propertyName=" + propertyName + ", propertyValue=" + propertyValue
		       + ", userCreated=" + userCreated + ", dateCreated=" + dateCreated + ", userUpdated=" + userUpdated
		       + ", dateUpdated=" + dateUpdated + "]";
	}
}
