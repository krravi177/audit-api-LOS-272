package com.xpanse.ims.model;

import jakarta.validation.constraints.NotBlank;

public class AuditRequest {
	
	@NotBlank
	private String applicationName;
	@NotBlank
	private String action;
	private String actionPayload;
	@NotBlank
	private String auditDate;
	
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getActionPayload() {
		return actionPayload;
	}
	public void setActionPayload(String actionPayload) {
		this.actionPayload = actionPayload;
	}
	public String getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}
	
	

}
