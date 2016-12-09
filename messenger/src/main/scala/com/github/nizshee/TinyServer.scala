package com.github.nizshee

import java.io.{DataInputStream, DataOutputStream}
import java.net.{InetAddress, ServerSocket, Socket}

import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.JsonDSL._


class TinyServer(interface: InetAddress, port: Int, state: => State, modState: (=> State => State) => Unit) {
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
            handle(socket)
          }
        } catch {
          case e: Exception => println("server stopped")
        }
      ).start()
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
  }

  private def handle(socket: Socket) = {
    try {
      val dis: DataInputStream = new DataInputStream(socket.getInputStream())
      val t = dis.readUTF()
      val j = parse(t)

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
      case e: Exception => e.printStackTrace()
    } finally {
      socket.close()
    }
  }

}
