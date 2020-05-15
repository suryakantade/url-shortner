package com.surya.crudproject.common.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {


  @Bean(name = "objectMapper")
  public ObjectMapper getObjectMapper() {
    return new ObjectMapper();
  }

  @Bean(name = "fileUtils")
  public FileUtils getFileUtils() {
    return new FileUtils();
  }

}
