package com.github.nizshee

/**
  * Class to store information about remote or local user.
  */
case class User(name: String, status: String)

object User {
  def empty = User("no name", "")
}
