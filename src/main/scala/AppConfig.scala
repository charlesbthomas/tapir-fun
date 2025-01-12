package dev.parvus

import pureconfig.ConfigReader

case class AppConfig(port: Int) derives ConfigReader
