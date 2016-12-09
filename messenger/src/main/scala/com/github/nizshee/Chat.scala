package com.github.nizshee

import com.typesafe.scalalogging.Logger

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global


/**
  * Class for state modification. It should be used in user interfaces.
  * @param mkServer Fabric method for Server handling request.
  * @param api Api for making request.
  */
class Chat(mkServer: (=> State, (=> State => State) => Unit) => Server, api: Api)(implicit logger: Logger) {

  @volatile private var state: State = State.empty
  @volatile private var callbacks: Seq[State => Unit] = Seq()
  val server = mkServer(state, modState)

  def registerCallback(callback: State => Unit) = this.synchronized { callbacks :+= callback; callback(state) }

  def start() = server.start()

  def stop() = server.stop()

  def updateName(name: String) = {
    logger.debug(s"update name: $name")
    modState(s => s.copy(localUser = s.localUser.copy(name = name)))
  }

  def updateStatus(status: String) = {
    logger.debug(s"update status: $status")
    modState(s => s.copy(localUser = s.localUser.copy(status = status)))
  }

  def updateHost(host: String) = {
    logger.debug(s"update host: $host")
    modState(_.copy(targetHost = host, history = Seq(), remoteUser = User.empty))
  }

  def updatePort(port: String) = {
    logger.debug(s"update port: $port")
    try {
      modState(_.copy(targetPort = port.toInt, history = Seq(), remoteUser = User.empty))
    } catch {
      case e: Exception =>
        logger.warn("update port", e)
        modState(_.copy(history = Seq(Message.System(s"${e.getMessage}"))))
    }
  }

  def updateTarget() = {
    val user = for {
      name <- api.getName(state.targetHost, state.targetPort)
      status <- api.getStatus(state.targetHost, state.targetPort)
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
    val message = api.sendMessage(state.targetHost, state.targetPort, state.localUser.name, msg)
     message.onComplete {
      case Failure(e) => modState(s => s.copy(history = Message.System(e.getMessage) +: s.history))
      case Success(e) => modState(s => s.copy(history = Message.Local(msg)  +: s.history))
    }
  }

  def getState = state

  private def modState(f: => State => State) = {
    this.synchronized {
      state = f(state)
    }
    callbacks.foreach(_(state))
  }
}
