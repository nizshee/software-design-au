package com.github.nizshee

import java.net.InetAddress

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

class Chat(interface: InetAddress, port: Int) {
  private var state: State = State.empty
  @volatile private var callbacks: Seq[State => Unit] = Seq()
  val tinyServer = new TinyServer(interface, port, state, modState)

  def registerCallback(callback: State => Unit) = this.synchronized { callbacks :+= callback; callback(state) }

  def start() = tinyServer.start()

  def stop() = tinyServer.stop()

  def updateName(name: String) = modState(s => s.copy(localUser = s.localUser.copy(name = name)))

  def updateStatus(status: String) = modState(s => s.copy(localUser = s.localUser.copy(status = status)))

  def updateHost(host: String) = modState(_.copy(targetHost = host, history = Seq(), remoteUser = User.empty))

  def updatePort(port: String) =
    try {
      modState(_.copy(targetPort = port.toInt, history = Seq(), remoteUser = User.empty))
    } catch {
      case e: Exception => modState(_.copy(history = Seq(Message.System(s"${e.getMessage}"))))
    }

  def updateTarget() = {
    val user = for {
      name <- Api.getName(state.targetHost, state.targetPort)
      status <- Api.getStatus(state.targetHost, state.targetPort)
    } yield {
      User(name, status)
    }
    user.onComplete {
      case Failure(e) =>
        e.printStackTrace()
        modState(s => s.copy(history = Message.System(e.getMessage) +: s.history))
      case Success(u) => modState(s => s.copy(remoteUser = u))
    }
  }

  def sendMessage(msg: String) = {
    val message = Api.sendMessage(state.targetHost, state.targetPort, state.localUser.name, msg)
     message.onComplete {
      case Failure(e) => modState(s => s.copy(history = Message.System(e.getMessage) +: s.history))
      case Success(e) => modState(s => s.copy(history = Message.Local(msg)  +: s.history))
    }
  }

  private def modState(f: => State => State) = {
    this.synchronized {
      state = f(state)
    }
    callbacks.foreach(_(state))
  }
}
