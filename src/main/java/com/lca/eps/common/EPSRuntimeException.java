package com.lca.eps.common;

import com.google.common.base.Throwables;

public class EPSRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public EPSRuntimeException(String message) {
		super(message);
	}
	
	public EPSRuntimeException (Exception e) {
			super(e!=null? Throwables.getStackTraceAsString(e): "Null Exception");
	}
}
