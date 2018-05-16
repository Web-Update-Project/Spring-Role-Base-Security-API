package com.spring.rolebase.security.api.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DELEGATE_TBL")
public class AccessDelegate {
	@Id
	@GeneratedValue
	private int serialId;
	public String addedDate;
	public int addedBy;
	public int moderatorId;

	public int getSerialId() {
		return serialId;
	}

	public void setSerialId(int serialId) {
		this.serialId = serialId;
	}

	public int getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(int addedBy) {
		this.addedBy = addedBy;
	}

	public int getModeratorId() {
		return moderatorId;
	}

	public void setModeratorId(int moderatorId) {
		this.moderatorId = moderatorId;
	}

	public String getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(String addedDate) {
		this.addedDate = addedDate;
	}

}
