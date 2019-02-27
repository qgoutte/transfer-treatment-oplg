package util

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import model.Heartbeat
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport {
  import DefaultJsonProtocol._

  implicit val heartbeatJsonFormat: RootJsonFormat[Heartbeat] = jsonFormat2(Heartbeat)
}
