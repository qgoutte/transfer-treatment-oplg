import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.concat
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import actor.{HeartbeatActor, TransferActor}
import route.{DefaultRoutes, TransferRoutes}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

object Server extends App with DefaultRoutes with TransferRoutes {

  implicit val system: ActorSystem = ActorSystem("transfer-treatment-oplg-server")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  val heartbeatActor: ActorRef = system.actorOf(HeartbeatActor.props, "heartbeatActor")
  val transferActor: ActorRef = system.actorOf(TransferActor.props, "transferActor")

  lazy val routes: Route = concat(heartbeatRoute, transferRoutes)

  val serverBinding: Future[Http.ServerBinding] = Http().bindAndHandle(routes, "localhost", 8080)

  serverBinding.onComplete {
    case Success(bound) =>
      println(s"Server online at http://${bound.localAddress.getHostString}:${bound.localAddress.getPort}/")
    case Failure(e) =>
      Console.err.println(s"Server could not start!")
      e.printStackTrace()
      system.terminate()
  }

  Await.result(system.whenTerminated, Duration.Inf)

}