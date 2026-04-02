package com.lca.eps.common.mappers;

import java.util.Date;

public class FileValidationRequestObject {

	private Date startDate;
	private Date endDate;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "FileValidationRequestObject [startDate=" + startDate + ", endDate=" + endDate + "]";
	}
}
