package com.peoplehum.urlshortener.common.util;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by peoplehum on 27/11/18.
 */
@Slf4j
public class ValidationUtility {

  /**
   * validates whether passed ids are not-null
   * @param ids
   * @return
   */
  public static Boolean validateIds(Long... ids) {
    log.debug("checking ids :{} for null check and negative check", (Object) ids);
    if (ids.length == 0) {
      return Boolean.FALSE;
    }
    for (Long id : ids) {
      if (id == null || id <= 0) {
        return Boolean.FALSE;
      }
    }
    return Boolean.TRUE;
  }

  /**
   * validate's whether sourceCustomerId has access to targetCustomer or not
   * @param sourceCustomerId
   * @param targetCustomerId
   * @return
   */
  public static Boolean validateCustomerScope(Long sourceCustomerId, Long targetCustomerId) {
    log.debug("validating customer scope for customer :{} on customer :{}", sourceCustomerId,
        targetCustomerId);
    Boolean result = Boolean.FALSE;
    if (sourceCustomerId.equals(targetCustomerId)) {
      result = Boolean.TRUE;
    }
    return result;
  }
}
