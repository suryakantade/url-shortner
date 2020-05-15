package com.surya.crudproject.v1.service.impl;


import com.surya.crudproject.v1.model.User;
import com.surya.crudproject.v1.repository.DummyRepository;
import com.surya.crudproject.v1.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("com.surya.crudproject.v1.service.impl.UserServiceImpl")
public class UserServiceImpl implements UserService {

  @Autowired
  @Qualifier("com.surya.crudproject.v1.repository.DummyRepository")
  private DummyRepository dummyRepository;

  @Override
  public Boolean isValidUser(User user) {
    Boolean isValidUser = Boolean.FALSE;
    if(getDummyUser().contains(user)){
      isValidUser = Boolean.TRUE;
    }
    return isValidUser;
  }

  private List<User> getDummyUser() {
    return dummyRepository.getAllDummyUser();
  }

}
