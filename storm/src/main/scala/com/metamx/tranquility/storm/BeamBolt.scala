/*
 * Licensed to Metamarkets Group Inc. (Metamarkets) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  Metamarkets licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.metamx.tranquility.storm

import java.{util => ju}

import com.metamx.common.scala.Logging
import com.metamx.tranquility.tranquilizer.{MessageDroppedException, Tranquilizer}
import org.apache.storm.task.{OutputCollector, TopologyContext}
import org.apache.storm.topology.OutputFieldsDeclarer
import org.apache.storm.topology.base.BaseRichBolt
import org.apache.storm.tuple.Tuple

import scala.language.implicitConversions

/**
  * A Storm Bolt for using a Beam to propagate tuples.
  *
  * @param beamFactory Factory for creating the Beam we will use.
  * @param batchSize   Maximum number of events to send per call to Beam.propagate.
  */
class BeamBolt[EventType](
                           beamFactory: BeamFactory[EventType],
                           batchSize: Int
                         ) extends BaseRichBolt with Logging {

  def this(beamFactory: BeamFactory[EventType]) = this(beamFactory, 2000)

  @volatile private var running: Boolean = false
  @volatile private var collector: OutputCollector = _
  @volatile private var tranquilizer: Tranquilizer[EventType] = _

  def prepare(conf: ju.Map[_, _], context: TopologyContext, collector: OutputCollector): Unit = {
    this.collector = collector
    this.tranquilizer = Tranquilizer.create(
      beamFactory.makeBeam(conf, context),
      batchSize,
      Tranquilizer.DefaultMaxPendingBatches,
      Tranquilizer.DefaultLingerMillis
    )
    this.tranquilizer.start()
    this.running = true
  }

  def execute(tuple: Tuple): Unit = {
    tranquilizer.send(tuple.getValue(0).asInstanceOf[EventType]) onSuccess {
      res =>
        collector.synchronized {
          collector.ack(tuple)
        }
    } onFailure {
      case e: MessageDroppedException =>
        collector.synchronized {
          collector.ack(tuple)
        }

      case e =>
        collector.synchronized {
          collector.fail(tuple)
        }
    }
  }

  override def cleanup(): Unit = {
    this.running = false
    tranquilizer.stop()
  }

  def declareOutputFields(declarer: OutputFieldsDeclarer): Unit = {}
}

