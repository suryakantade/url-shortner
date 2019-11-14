package com.shortener.urlshortener.common.exception.error;

public enum ErrorCode implements AppCode<ErrorCode> {

		// @formatter:off
		SUCCESS(1000, "SUCCESS"),
		FAIL(1001, "fail"),
	PROCESSING_ERROR(999, "PROCESSING_ERROR"),
		ACCESS_DENIED(401,"ACCESS_DENIED"),
		INVALID_DATA(400,"INVALID_DATA");
        // @formatter:on
		
		private final int code;

		private final String desc;

	  ErrorCode(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

	/**
	 * Return the integer code of this status code.
	 */
	@Override
	public int getCode() {
		return this.code;
	}

	/**
	 * Return the reason phrase of this status code.
	 */
	@Override
	public String getDesc() {
		return desc;
	}

	/**
	 * Return a string representation of this status code.
	 */
	@Override
	public String toString() {
		return Integer.toString(code);
	}


	@Override
	public ErrorCode valueOf(int statusCode) {
		for (ErrorCode status : values()) {
			if (status.code == statusCode) {
				return status;
			}
		}
		throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
	}
}
