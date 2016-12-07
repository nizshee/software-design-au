package com.github.nizshee


case class User(name: String, status: String)

object User {
  def empty = User("no name", "")
}
