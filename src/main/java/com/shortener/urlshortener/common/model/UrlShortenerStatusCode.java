package com.shortener.urlshortener.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nethum.errorhandling.exception.error.AppCode;
import com.nethum.errorhandling.exception.error.CodeDesc;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by peoplehum on 21/11/18.
 */
public enum UrlShortenerStatusCode implements AppCode<UrlShortenerStatusCode> {

  SUCCESS(1000, "SUCCESS"),
  PROCESSING_ERROR(999, "PROCESSING_ERROR"),
  DATA_VALIDATION_FAILED(45001, "DATA_VALIDATION_FAILED"),
  INVALID_CUSTOMER_SCOPE(45002, "INVALID_CUSTOMER_SCOPE");

  private static Map<Integer, UrlShortenerStatusCode> FORMAT_MAP = Stream.of(UrlShortenerStatusCode.values())
      .collect(Collectors.toMap(s -> s.code, Function.identity()));

  private final int code;

  private final String desc;

  UrlShortenerStatusCode(int code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  @JsonCreator // This is the factory method and must be static
  public static UrlShortenerStatusCode fromJson(CodeDesc codeDesc) {
    return Optional.ofNullable(FORMAT_MAP.get(codeDesc.getCode()))
        .orElseThrow(() -> new IllegalArgumentException(codeDesc.toString()));
  }

  // validity of status code
  public static Boolean validType(String type) {
    if (FORMAT_MAP.containsKey(type)) {
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
  }
  /**
   * Return the integer code of this status code.
   */
  public int getCode() {
    return this.code;
  }

  /**
   * Return the reason phrase of this status code.
   */
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

  /**
   * @param statusCode
   * @return
   */
  @Override
  public UrlShortenerStatusCode valueOf(int statusCode) {
    for (UrlShortenerStatusCode status : values()) {
      if (status.code == statusCode) {
        return status;
      }
    }
    throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
  }
}
