package com.accenture.aesrefund.common;

import com.accenture.aesrefund.constant.HttpStatusEnum;

import java.io.IOException;

public class CommonWebServiceException extends IOException {

	private static final long serialVersionUID = -7929408390606206016L;

	private String errorCode;
	private String source;
	private HttpStatusEnum httpStatusEnum;
	
	public CommonWebServiceException(String message, String errorCode) {
	  super(message);
	  this.errorCode = errorCode;
	}

//	public CommonWebServiceException(Throwable cause, String errorCode) {
//	  super(cause);
//	  this.errorCode = errorCode;
//	}
//
//	public CommonWebServiceException(String message, Throwable cause, String errorCode) {
//	  super(message, cause);
//	  this.errorCode = errorCode;
//	}
	
	public CommonWebServiceException(HttpStatusEnum httpStatusEnum, String source) {
		  //super(httpStatusEnum.getMessage());
		  //this.errorCode = errorCode;
		  this.source = source;
		  this.httpStatusEnum = httpStatusEnum;
		}

	public String getErrorCode() {
	  return errorCode;
	}
	
	public String getSource() {
		  return source;
	}
	
	public HttpStatusEnum getHttpStatusEnum() {
		  return httpStatusEnum;
	}
	@Override
	public Throwable fillInStackTrace()
	{
		return this;
	}
}
