package util

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import actor.TransferActor.ActionPerformed
import actor.{Heartbeat, Transfer, Transfers}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport {
  import DefaultJsonProtocol._

  implicit val heartbeatJsonFormat: RootJsonFormat[Heartbeat] = jsonFormat2(Heartbeat)

  implicit val transferJsonFormat: RootJsonFormat[Transfer] = jsonFormat6(Transfer)
  implicit val transfersJsonFormat: RootJsonFormat[Transfers] = jsonFormat1(Transfers)

  implicit val actionPerformedJsonFormat: RootJsonFormat[ActionPerformed] = jsonFormat1(ActionPerformed)
}
