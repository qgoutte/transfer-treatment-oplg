package route

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging
import akka.http.scaladsl.server.Directives.pathPrefix
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.pattern.ask
import akka.util.Timeout
import actor.Heartbeat
import actor.HeartbeatActor.GetHeartbeat
import util.JsonSupport

import scala.concurrent.Future
import scala.concurrent.duration._

trait DefaultRoutes extends JsonSupport {

  implicit def system: ActorSystem

  private lazy val log = Logging(system, classOf[DefaultRoutes])

  def heartbeatActor: ActorRef

  implicit lazy val timeout: Timeout = Timeout(5, SECONDS)

  lazy val heartbeatRoute: Route =
    pathPrefix("heartbeat") {
      get {
        val heartbeat: Future[Heartbeat] =
          (heartbeatActor ? GetHeartbeat).mapTo[Heartbeat]
        complete(heartbeat)
      }
    }

}
