package com.lca.eps.common;

import com.google.common.base.Throwables;

public class EPSException extends Exception {
	public EPSException(String message) {
		super(message);
	}
	
	public EPSException (Exception e) {
			super(e!=null? Throwables.getStackTraceAsString(e): "Null Exception");
	}
}
