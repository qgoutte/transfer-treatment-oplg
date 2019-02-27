package model

import akka.actor.{ Actor, ActorLogging, Props }

final case class Heartbeat(code: Int, status: String)

object HeartbeatActor {
  final case object GetHeartbeat

  def props: Props = Props[HeartbeatActor]
}

class HeartbeatActor extends Actor with ActorLogging {
  import HeartbeatActor._

  def receive: Receive = {
    case GetHeartbeat =>
      sender() ! Heartbeat(200, "UP AND RUNNING")
  }
}
