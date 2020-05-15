package com.surya.crudproject.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.surya.crudproject.common.exception.error.AppCode;
import com.surya.crudproject.common.exception.error.CodeDesc;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public enum CrudProjectStatusCode implements AppCode<CrudProjectStatusCode> {

  SUCCESS(1000, "SUCCESS"),
  PROCESSING_ERROR(999, "PROCESSING_ERROR"),
  DATA_VALIDATION_FAILED(1100, "DATA_VALIDATION_FAILED"),
  FILE_CONTENT_MAPPING_FAILED(1101, "FILE_CONTENT_MAPPING_FAILED"),
  STRING_MAPPING_FAILED(1102, "STRING_MAPPING_FAILED"),
  INVALID_USER(1103, "INVALID_USER"),
  INVALID_LOCATION_SCOPE(1104, "INVALID_LOCATION_SCOPE");

  private static Map<Integer, CrudProjectStatusCode> FORMAT_MAP =
      Stream.of(CrudProjectStatusCode.values())
          .collect(Collectors.toMap(s -> s.code, Function.identity()));

  private final int code;

  private final String desc;

  CrudProjectStatusCode(int code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  @JsonCreator // This is the factory method and must be static
  public static CrudProjectStatusCode fromJson(CodeDesc codeDesc) {
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
   *
   * @return
   */
  @Override
  public CrudProjectStatusCode valueOf(int statusCode) {
    for (CrudProjectStatusCode status : values()) {
      if (status.code == statusCode) {
        return status;
      }
    }
    throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
  }
}
