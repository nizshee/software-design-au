package com.github.nizshee.executor

/** Class that keeps information about world.
  *
  * @constructor to create new [[com.github.nizshee.executor.Context]]
  * @param environment variables keeping in map
  * @param executor interface to execute commands
  */
case class Context(environment: Map[String, String], executor: Executor)
