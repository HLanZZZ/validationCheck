package com.accenture.aesrefund.common;


import java.io.IOException;

public class InputParameterException extends IOException {
  /**
	 * 
	 */
	private static final long serialVersionUID = 3595946834725699415L;


public static final String ERR_INPUT_PARAMETER = "Parameter error";


	private final String errorCode;
	private final String detail;
	
	public InputParameterException(String message, String errorCode, String detail) {
	  super(message);
	  this.errorCode = errorCode;
	  this.detail=detail;
	}

	public InputParameterException(Throwable cause, String errorCode, String detail) {
	  super(cause);
	  this.errorCode = errorCode;
	  this.detail=detail;
	}

	public InputParameterException(String message, Throwable cause, String errorCode, String detail) {
	  super(message, cause);
	  this.errorCode = errorCode;
	  this.detail=detail;
	}

	public String getErrorCode() {
	  return errorCode;
	}
	
	public String getDetail() {
		  return detail;
		}
	@Override
	public Throwable fillInStackTrace()
	{
		return this;
	}
}
