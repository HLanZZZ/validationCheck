package com.accenture.aesrefund.common;

import java.io.IOException;

public class ErrorResponseException extends IOException {
  public static final String ERR_CONSTRUCT_REQUEST_HEADER = "ERR_CONSTRUCT_REQUEST_HEADER";
  public static final String ERR_CONSTRUCT_REQUEST_MESSAGE = "ERR_CONSTRUCT_REQUEST_MESSAGE";
  public static final String ERR_1A_EMPTY_RESPONSE = "ERR_1A_EMPTY_RESPONSE";
  public static final String ERR_1A_TIME_OUT = "ERR_1A_TIME_OUT";
  public static final String ERR_1A_ERROR_RESPNSE="ERR_1A_RRROR_RESPONSE";

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = -7929408390606206016L;

	private final String errorCode;
	private final String detail;
	
	public ErrorResponseException(String message, String errorCode, String detail) {
	  super(message);
	  this.errorCode = errorCode;
	  this.detail=detail;
	}

	public ErrorResponseException(Throwable cause, String errorCode, String detail) {
	  super(cause);
	  this.errorCode = errorCode;
	  this.detail=detail;
	}

	public ErrorResponseException(String message, Throwable cause, String errorCode, String detail) {
	  super(message, cause);
	  this.errorCode = errorCode;
	  this.detail=detail;
	}
	
	public String getErrorCode() {
	  return errorCode;
	}
	
	public String getDetail()
	{
		return detail;
	}
	@Override
	public Throwable fillInStackTrace()
	{
		return this;
	}
}
