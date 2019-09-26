package com.example.demo.web.contract;

/**
 * 不重要的异常
 */
public class UnimportantException extends ServiceException {

	private static final long serialVersionUID = -3648285599459056694L;

	public UnimportantException() {
	}

	public UnimportantException(String message) {
		super(message);
		setResultCode(API_OK);
	}
	
	public UnimportantException(String message, String statusCode) {
		super(message, statusCode);
		setResultCode(API_OK);
	}
	
	public UnimportantException(String message, Throwable cause) {
		super(message, cause);
		setResultCode(API_OK);
	}

	public UnimportantException(String message, String statusCode, Throwable cause) {
		super(message, statusCode, cause);
		setResultCode(API_OK);
	}
}
