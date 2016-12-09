package com.github.nizshee

import java.io.{DataInputStream, DataOutputStream}
import java.net.{InetAddress, ServerSocket, Socket, SocketException}

import com.typesafe.scalalogging.Logger
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.JsonDSL._


/**
  * Implementation of Server.
  */
class JsonServer(interface: InetAddress, port: Int, state: => State, modState: (=> State => State) => Unit)(implicit logger: Logger)
  extends Server {

  private var serverSocket: ServerSocket = _
  @volatile private var isRunning: Boolean = false
  implicit val formats = DefaultFormats

  def start() = {
    this.synchronized {
      serverSocket = new ServerSocket(port, 50, interface)
      isRunning = true
      new Thread(() =>
        try {
          while (true) {
            val socket = serverSocket.accept()
            logger.debug("handle request")
            handle(socket)
          }
        } catch {
          case e: SocketException if e.getMessage == "Socket closed" =>
          case e: Exception => logger.error("server", e)
        }
      ).start()
      logger.debug("server started")
    }
  }

  def stop() = {
    this.synchronized {
      if (isRunning) {
        serverSocket.close()
        serverSocket = null
        isRunning = false
      }
    }
    logger.debug("server stopped")
  }

  private def handle(socket: Socket) = {
    try {
      val dis: DataInputStream = new DataInputStream(socket.getInputStream())
      val t = dis.readUTF()
      val j = parse(t)

      logger.debug(s"message: $t")
      val response: JValue = (j \ "type").extract[String] match {
        case "getStatus" => "status" -> state.localUser.status
        case "getName" => "name" -> state.localUser.name
        case "sendMessage" =>
          modState(s =>
            s.copy(
              history = Message.Remote((j \ "text").extract[String]) +: s.history,
              remoteUser = s.remoteUser.copy(name = (j \ "name").extract[String])
            )
          )
          "result" -> "ok"
      }
      new DataOutputStream(socket.getOutputStream()).writeUTF(compact(response))
    } catch {
      case e: Exception => logger.warn("handle request", e)
    } finally {
      socket.close()
    }
  }

}
