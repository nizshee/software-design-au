package com.github.nizshee

import com.typesafe.scalalogging.Logger
import org.scalatest._

import scala.concurrent.Future


class ChatSpec extends FlatSpec with Matchers {
  implicit val logger = Logger("test")

  var modState: (=> State => State) => Unit = _
  var state: () => State = _
  @volatile var isSentMessage: Boolean = _
  val remoteName = "djweoijdoi32"
  val remoteStatus = "lkcnaeknf"
  val name = "dmwekndflowike"
  val status = "dlkwnefcjnwe"
  val port = "5478"
  val host = "ckwmnelkrfcne"
  val msg = "mxdwekcnwke"

  def mkServer(state1: => State, modState1: (=> State => State) => Unit) = new Server {
    modState = modState1
    state = () => state1

    override def start(): Unit = ()

    override def stop(): Unit = ()
  }

  val api = new Api {
    override def getName(host: String, port: Int): Future[String] = Future.successful(remoteName)

    override def getStatus(host: String, port: Int): Future[String] = Future.successful(remoteStatus)

    override def sendMessage(host: String, port: Int, name: String, msg1: String): Future[Unit] = {
      msg1 should be (msg)
      isSentMessage = true
      Future.successful(())
    }
  }

  val chat = new Chat(mkServer, api)

  "Chat" should "update status" in {
    chat.updateStatus(status)
    state().localUser.status should be (status)
  }

  it should "update name" in {
    chat.updateName(name)
    state().localUser.name should be (name)
  }

  it should "update host" in {
    chat.updateHost(host)
    state().targetHost should be (host)
  }

  it should "update port" in {
    chat.updatePort(port)
    state().targetPort should be (port.toInt)
  }

  it should "ask api for name and status" in {
    chat.updateTarget()
    Thread sleep 100
    state().remoteUser should be (User(remoteName, remoteStatus))
  }

  it should "send messages" in {
    chat.sendMessage(msg)
    Thread sleep 100
    isSentMessage should be (true)
  }

  it should "be modifiable by server" in {
    modState(_ => State.empty)
    state() should be (State.empty)
  }
}
