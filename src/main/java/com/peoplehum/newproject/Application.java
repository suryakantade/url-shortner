package com.peoplehum.newproject;

import net.javacrumbs.shedlock.core.DefaultLockManager;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.LockableTaskScheduler;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import net.javacrumbs.shedlock.spring.internal.SpringLockConfigurationExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.sql.DataSource;
import java.time.Duration;

/**
 * Created by punit on 11/10/17.
 */
@EnableSwagger2
@SpringBootApplication(exclude = WebMvcAutoConfiguration.class,
    scanBasePackages = {"com.peoplehum.newproject", "com.nethum.errorhandling",
        "com.peoplehum.security"})
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

  @Bean
  public LockProvider lockProvider(DataSource dataSource) {
    return new JdbcTemplateLockProvider(dataSource);
  }

}
