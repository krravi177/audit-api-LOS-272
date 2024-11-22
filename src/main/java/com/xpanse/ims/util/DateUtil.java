package com.xpanse.ims.util;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class DateUtil {

	private final SimpleDateFormat auditRequestFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	private final SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	

	public SimpleDateFormat getAuditFormat() {
		return auditRequestFormat;
	}
	
	public SimpleDateFormat getSqlFormat() {
		return sqlFormat;
	}
	
}