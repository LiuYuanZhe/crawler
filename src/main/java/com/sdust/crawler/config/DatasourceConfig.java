package com.sdust.crawler.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * desc.
 */
@Configuration
public class DatasourceConfig {

  @Value("${jdbc.driver}")
  private String jdbcDriver;
//          = "com.mysql.jdbc.Driver";
  @Value("${jdbc.url}")
  private String jdbcUrl;
//        = "jdbc:mysql://localhost:3306/ssm?characterEncoding=utf-8";
  @Value("${jdbc.username}")
  private String jdbcUsername;
//  = "root";
  @Value("${jdbc.password}")
  private String jdbcPassword;
//  = "870873";
  @Value("${hikari.connection-timeout}")
  private long connectionTimeout;
//  = 10000;
  @Value("${hikari.auto-commit}")
  private boolean autoCommit;
//  = false;
  @Value("${hikari.pool-name}")
  private String poolName;
//  = "price";
  @Value("${hikari.maximum-pool-size}")
  private int maximumPoolSize;
//  = 10;
  @Value("${hikari.minimum-idle}")
  private int minimumIdle;
//          = 1;

  /**
   * 数据源的配置，为读配置中心，覆盖Spring自动配置的数据源.
   *
   * @return HikariDataSource
   */
  @Bean
  public HikariDataSource dataSource() {
    HikariConfig config = new HikariConfig();
    config.setDriverClassName(jdbcDriver);
    config.setJdbcUrl(jdbcUrl);
    config.setUsername(jdbcUsername);
    config.setPassword(jdbcPassword);
    config.setConnectionTimeout(connectionTimeout);
    config.setAutoCommit(autoCommit);
    config.setPoolName(poolName);
    config.setMaximumPoolSize(maximumPoolSize);
    config.setMinimumIdle(minimumIdle);
    HikariDataSource dataSource = new HikariDataSource(config);
    return dataSource;
  }

}
