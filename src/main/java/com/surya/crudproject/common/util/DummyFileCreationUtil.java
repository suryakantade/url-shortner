package com.surya.crudproject.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.surya.crudproject.v1.model.Location;
import com.surya.crudproject.v1.model.User;

import java.util.Arrays;
import java.util.List;

public class DummyFileCreationUtil {

  public static void main(String[] args) {

    /*User user =  User.builder().id(1l).emailId("abc.a@gmail.com").name("surya").build();
    User user1 =  User.builder().id(2l).emailId("abc.b@gmail.com").name("suryb").build();
    User user2=  User.builder().id(3l).emailId("abc.c@gmail.com").name("suryc").build();
    User user3 =  User.builder().id(4l).emailId("abc.d@gmail.com").name("suryd").build();
    User user4 =  User.builder().id(5l).emailId("abc.e@gmail.com").name("surye").build();
    User user5 =  User.builder().id(6l).emailId("abc.f@gmail.com").name("suryf").build();

    List<User> users = Arrays.asList(user, user1, user2, user3, user4, user5);
    try {
      System.out.println(new ObjectMapper().writeValueAsString(users));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }*/


    Location location = Location.builder().id(1l).name("kolkata").build();
    Location location1 = Location.builder().id(2l).name("delhi").build();
    Location location2 = Location.builder().id(3l).name("mumbai").build();
    Location location3 = Location.builder().id(4l).name("darjeeling").build();
    List<Location> locations = Arrays.asList(location, location1, location2, location3);
    try {
      System.out.println(new ObjectMapper().writeValueAsString(locations));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

  }


}
