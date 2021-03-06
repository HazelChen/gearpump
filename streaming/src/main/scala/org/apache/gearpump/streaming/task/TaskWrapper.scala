/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package org.apache.gearpump.streaming.task

import akka.actor.{ActorSystem, Cancellable, Props, ActorRef}
import org.apache.gearpump.Message
import org.apache.gearpump.cluster.UserConfig
import org.apache.gearpump.streaming.DAG
import org.apache.gearpump.util.LogUtil

import scala.concurrent.duration.FiniteDuration

/**
 * This provides TaskContext for user defined tasks
 * @param taskClass
 * @param context
 * @param userConf
 */
class TaskWrapper(taskClass: Class[_ <: Task], context: TaskContextData, userConf: UserConfig) extends TaskContext with TaskInterface {

  private val LOG = LogUtil.getLogger(getClass)

  private var actor: TaskActor = null

  private var task: Task = null

  def setTaskActor(actor: TaskActor): Unit = this.actor = actor

  override def taskId: TaskId = context.taskId

  override def appId: Int = context.appId

  override def appName: String = context.appName

  override def executorId: Int = context.executorId

  override def parallelism: Int = context.parallelism

  override def appMaster: ActorRef = context.appMaster

  override def dag: DAG = context.dag

  override def output(msg: Message): Unit = actor.output(msg)

  def self: ActorRef = actor.context.self

  def system: ActorSystem = actor.context.system

  /**
   * @see ActorRefProvider.actorOf
   */
  override def actorOf(props: Props): ActorRef = actor.context.actorOf(props)

  /**
   * @see ActorRefProvider.actorOf
   */
  override def actorOf(props: Props, name: String): ActorRef = actor.context.actorOf(props, name)

  override def onStart(startTime: StartTime): Unit = {
    if (null != task) {
      LOG.error("Task.onStart should not be called multiple times...")
    }
    val constructor = taskClass.getConstructor(classOf[TaskContext], classOf[UserConfig])
    task = constructor.newInstance(this, userConf)
    task.onStart(startTime)
  }

  override def onNext(msg: Message): Unit = task.onNext(msg)

  override def onStop(): Unit = {
    task.onStop()
    task = null
  }

  def schedule(initialDelay: FiniteDuration, interval: FiniteDuration)(f: ⇒ Unit): Cancellable = {
    val dispatcher = actor.context.system.dispatcher
    actor.context.system.scheduler.schedule(initialDelay, interval)(f)(dispatcher)
  }
}
