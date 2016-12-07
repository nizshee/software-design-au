package com.github.nizshee


import java.net.InetAddress

import scalafx.application.JFXApp


object Main extends JFXApp {

  val host = parameters.named.getOrElse("h", "localhost")
  val port = parameters.named.getOrElse("p", "12345")

  val chat = new Chat(InetAddress.getByName(host), port.toInt)

  stage = UI(stage)(chat)

}
