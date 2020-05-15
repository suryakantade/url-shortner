package com.surya.crudproject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@SpringBootApplication(scanBasePackages = {"com.surya.crudproject"})
@EnableAutoConfiguration(
    exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  // max. number of threads running when the queue is full, default queue capacity is INTEGER_MAX
  @Value("${thread.pool.max:20}")
  private Integer maxThreadPool;

  // number of threads running at a time
  @Value("${thread.pool.max:10}")
  private Integer corePoolSize;

  @Bean(name = "defaultTaskExecutor")
  public ThreadPoolTaskExecutor defaultTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(corePoolSize);
    executor.setMaxPoolSize(maxThreadPool);
    return executor;
  }
}
