import actor.HeartbeatActor
import akka.actor.ActorRef
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import route.DefaultRoutes

class DefaultRoutesSpec extends WordSpec with Matchers with ScalaFutures with ScalatestRouteTest with DefaultRoutes {

  override val heartbeatActor: ActorRef = system.actorOf(HeartbeatActor.props, "heartbeatActor")

  lazy val routes: Route = heartbeatRoute

  "DefaultRoutes" should {
    "return heartbeat response on GET /heartbeat" in {
      val request = HttpRequest(uri = "/heartbeat")
      request ~> routes ~> check {
        status should ===(StatusCodes.OK)
        contentType should ===(ContentTypes.`application/json`)
        entityAs[String] should ===("""{"code":200,"status":"UP AND RUNNING"}""")
      }
    }
  }

}
