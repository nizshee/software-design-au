package com.github.nizshee

import scala.concurrent.Future


/**
  * Interface for interaction with server. It depends on data coding protocol.
  */
trait Api {
  def getStatus(host: String, port: Int): Future[String]

  def getName(host: String, port: Int): Future[String]

  def sendMessage(host: String, port: Int, name: String, msg: String): Future[Unit]
}
