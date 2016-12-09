package com.github.nizshee


/**
  * Server that should wait for incoming connection and handle them.
  */
trait Server {
  def start(): Unit

  def stop(): Unit
}
