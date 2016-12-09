package com.github.nizshee

import java.io.{DataInputStream, DataOutputStream}
import java.net.{InetAddress, Socket}

import com.typesafe.scalalogging.Logger
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.JsonDSL._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


/**
  * Implementation of Api.
  */
class JsonApi(implicit logger: Logger) extends Api {

  implicit val formats = DefaultFormats

  def getStatus(host: String, port: Int): Future[String] = send(
    host,
    port,
    "type" -> "getStatus",
    j => (j \ "status").extract[String]
  )

  def getName(host: String, port: Int): Future[String] = send(
    host,
    port,
    "type" -> "getName",
    j => (j \ "name").extract[String]
  )

  def sendMessage(host: String, port: Int, name: String, msg: String): Future[Unit] = send(
    host,
    port,
    ("type" -> "sendMessage") ~ ("port" -> port) ~ ("name" -> name) ~ ("text" -> msg),
    j => assert((j \ "result").extract[String] == "ok", "Response not ok.")
  )

  private def send[T](host: String, port: Int, jValue: JValue, f: JValue => T): Future[T] = Future {
    logger.debug(s"send request: $jValue")
    val socket = new Socket(InetAddress.getByName(host), port)
    try {
      new DataOutputStream(socket.getOutputStream()).writeUTF(compact(jValue))
      f(parse(new DataInputStream(socket.getInputStream()).readUTF()))
    } finally {
      socket.close()
    }
  }
}
