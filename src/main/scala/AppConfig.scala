package dev.parvus

import pureconfig.ConfigReader
import com.zaxxer.hikari.HikariConfig

case class AppConfig(port: Int) derives ConfigReader

case class DatabaseInstance(
    url: String,
    username: String,
    password: String,
    driver: String,
    poolSize: Int
) derives ConfigReader:
  def toHikariConfig: HikariConfig =
    val config = new HikariConfig()
    config.setJdbcUrl(url)
    config.setUsername(username)
    config.setPassword(password)
    config.setDriverClassName(driver)
    config.setMaximumPoolSize(poolSize)
    config
