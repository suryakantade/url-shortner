package com.surya.crudproject.common.exception.error;


import com.fasterxml.jackson.annotation.JsonValue;

/**
 * To be used as Mixin interface  for any Enum trying to set {@link AppErrorObject#status}
 * @apiNote enum implementing {@link AppCode} must provide a valid implementation of methods in {@link AppCode}
 * @param <T> should be an Enum
 * @see <a href="https://en.wikipedia.org/wiki/Mixin">Mixin</a>
 * @see com.nethum.errorhandling.exception.error.AppErrorObject
 */
public interface AppCode<T extends Enum<T>> {
  /**
   * To get the {@link T extends Enum} associated with status code
   * @param statusCode
   * @return
   */
  T valueOf(int statusCode);

  /**
   * Method  to serialize {@link ErrorCode#code}
   * @return
   */
  int getCode();

  /**
   *Method  to serialize {@link ErrorCode#desc}
   * @return
   */
  String getDesc();

  /**
   * Default Implementation lets end service to create json in format:
   *<p> {
      "status": {
            "code": 8,
            "desc": "empty request"
           }
        }
   </p>
   * @return
   */
  @JsonValue
  default CodeDesc toJson() {
    CodeDesc codeDesc = new CodeDesc();
    codeDesc.setDesc(getDesc());
    codeDesc.setCode(getCode());
    return codeDesc;
  }

}
