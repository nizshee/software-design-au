package com.github.nizshee

import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.{Button, TextField, TextInputDialog}
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.paint.Color._
import scalafx.scene.text.Text
import scalafx.Includes._


object UI {

  def apply(stage: PrimaryStage)(chat: Chat) = {
    val sceneWidth = 640
    val sceneHeight = 480

    val sendWidth = 50

    val status = new Text {
      text = ""
    }

    val changeName = new Button {
      text = "name"
      onMouseClicked = { _: MouseEvent =>
        new TextInputDialog(defaultValue = "walter") {
          initOwner(stage)
          title = "Change Name Dialog"
          headerText = "Change name."
          contentText = "Please enter your name:"
        }.showAndWait() match {
          case Some(name) => chat.updateName(name)
          case None =>
        }
      }
    }

    val changeStatus = new Button {
      text = "status"
      onMouseClicked = { _: MouseEvent =>
        new TextInputDialog(defaultValue = "") {
          initOwner(stage)
          title = "Change Status Dialog"
          headerText = "Change status."
          contentText = "Please enter your status:"
        }.showAndWait() match {
          case Some(status1) => chat.updateStatus(status1)
          case None =>
        }
      }
    }

    val changeHost = new Button {
      text = "host"
      onMouseClicked = { _: MouseEvent =>
        new TextInputDialog(defaultValue = "") {
          initOwner(stage)
          title = "Change Host Dialog"
          headerText = "Change host."
          contentText = "Please enter target host:"
        }.showAndWait() match {
          case Some(host) => chat.updateHost(host)
          case None =>
        }
      }
    }

    val changePort = new Button {
      text = "port"
      onMouseClicked = { _: MouseEvent =>
        new TextInputDialog(defaultValue = "") {
          initOwner(stage)
          title = "Change Port Dialog"
          headerText = "Change port."
          contentText = "Please enter target port:"
        }.showAndWait() match {
          case Some(port) => chat.updatePort(port)
          case None =>
        }
      }
    }

    val target = new Text {
      text = ""
    }

    val checkTarget = new Button {
      text = "check status"
      onMouseClicked = { _: MouseEvent =>
        chat.updateTarget()
      }
    }

    val input = new TextField {
      maxWidth = sceneWidth - sendWidth
      minWidth = sceneWidth - sendWidth
    }

    val send = new Button {
      text = "send"
      maxWidth = sendWidth
      minWidth = sendWidth
      onMouseClicked = () => {
        val text = input.text.value
        input.text = ""
        chat.sendMessage(text)
      }
    }

    val textArea = new Text {
      text = ""
    }

    chat.registerCallback { state =>
      target.text = s"{name = ${state.remoteUser.name}; status = ${state.remoteUser.status}}"
      textArea.text = state.history.map {
        case Message.Local(msg) => s"[${state.localUser.name}]$msg"
        case Message.Remote(msg) => s"[${state.remoteUser.name}]$msg"
        case Message.System(msg) => msg
      } mkString "\n"
      status.text = s"Options {name = ${state.localUser.name}; status = ${state.localUser.status}; " +
        s"host = ${state.targetHost}; port = ${state.targetPort}}"
    }


    new PrimaryStage {
      onShown = () => chat.start()
      onCloseRequest = () => chat.stop()

      title = "Messenger"
      height = sceneHeight
      width = sceneWidth
      scene = new Scene {
        fill = White
        content = new VBox {
          children = Seq(
            status,
            new HBox {
              children = Seq(
                changeName,
                changeStatus,
                changeHost,
                changePort
              )
            },
            target,
            checkTarget,
            new Text("Chat"),
            new HBox {
              children = Seq(
                input,
                send
              )
            },
            textArea
          )
        }
      }
    }
  }
}
