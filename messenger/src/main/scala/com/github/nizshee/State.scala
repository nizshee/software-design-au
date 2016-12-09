package com.github.nizshee


/**
  * Class to store all information.
  * @param localUser Local user info.
  * @param history Sequence of previous messages.
  * @param targetHost Host you want to send messages.
  * @param targetPort Host's port.
  * @param remoteUser Current information about remote user.
  */
case class State(localUser: User,
                 history: Seq[Message],
                 targetHost: String,
                 targetPort: Int,
                 remoteUser: User)

object State {
  def empty = State(User("local", ""), Seq(), "localhost", 12345, User("remote", ""))
}
