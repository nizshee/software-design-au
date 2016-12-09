package com.github.nizshee

import java.net.InetAddress

import org.scalatest._

import scala.concurrent.duration._
import scala.concurrent.Await

class ServerApiSpec extends FlatSpec with BeforeAndAfterAll with Matchers {
  val serverHost = "localhost"
  val serverPort = 12345
  val apiHost = "localhost"
  val apiPort = 12346

  val localName = "123csdcswed"
  val localStatus = "ckjnbkrec"
  val remoteName = "dknewlkdn"
  val remoteStatus = "nf3j4ofnc"
  val history = Seq(Message.Local("12312"), Message.Remote("kjsbfkjew"), Message.System("jdksneck"))

  @volatile var state = State(
    localUser = User(localName, localStatus),
    history = history,
    targetHost = apiHost,
    targetPort = apiPort,
    remoteUser = User(remoteName, remoteStatus)
  )

  def modState(f: => State => State) = {
    this.synchronized {
      state = f(state)
    }
  }

  val server = new JsonServer(InetAddress.getByName(serverHost), serverPort, state, modState)

  override protected def beforeAll(): Unit = {
    server.start()
  }

  override protected def afterAll(): Unit = {
    server.stop()
  }

  "ServerApi" should "check name" in {
    val future = JsonApi.getName(serverHost, serverPort)
    val result = Await.result(future, 5.second)
    result should be (localName)
  }

  it should "check status" in {
    val future = JsonApi.getStatus(serverHost, serverPort)
    val result = Await.result(future, 5.second)
    result should be (localStatus)
  }

  it should "send messages" in {
    val msg = "cjdsnvcjs"
    val future = JsonApi.sendMessage(serverHost, serverPort, remoteName, msg)
    val result = Await.result(future, 5.second)
    result should be (())
    state.history should be (Message.Remote(msg) +: history)
  }
}
