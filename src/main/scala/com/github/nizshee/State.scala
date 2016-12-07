package com.github.nizshee


case class State(localUser: User,
                 history: Seq[Message],
                 targetHost: String,
                 targetPort: Int,
                 remoteUser: User)

object State {
  def empty = State(User("local", ""), Seq(), "localhost", 12345, User("remote", ""))
}