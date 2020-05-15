package com.surya.crudproject.v1.service;

import com.surya.crudproject.v1.model.User;

public interface UserService {

  /**
   *
   * @param user
   * @return
   */
  public Boolean isValidUser(User user);
}
