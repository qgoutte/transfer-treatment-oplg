package route

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.{delete, get, post, put}
import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.pattern.ask
import akka.util.Timeout
import actor.TransferActor._
import actor.{Transfer, TransferStatus, Transfers}
import util.JsonSupport

import scala.concurrent.Future
import scala.concurrent.duration._

trait TransferRoutes extends JsonSupport {

  implicit def system: ActorSystem

  lazy val log = Logging(system, classOf[TransferRoutes])

  def transferActor: ActorRef

  private implicit lazy val timeout: Timeout = Timeout(5, SECONDS)

  lazy val transferRoutes: Route =
    pathPrefix("transfers") {
      concat(
        pathEnd {
          concat(
            get {
              val transfers: Future[Transfers] = (transferActor ? GetTransfers).mapTo[Transfers]
              complete(transfers)
            },
            post {
              entity(as[Transfer]) { transfer =>
                val transferCreated: Future[ActionPerformed] =
                  (transferActor ? CreateTransfer(transfer)).mapTo[ActionPerformed]
                onSuccess(transferCreated) { performed =>
                  log.info("Created transfer [{}]: {}", transfer.id, performed.description)
                  complete((StatusCodes.Created, performed))
                }
              }
            }
          )
        },
        path(Segment) { id =>
          concat(
            get {
              val transfer: Future[Transfer] =
                (transferActor ? GetTransfer(id.toInt)).mapTo[Transfer]
              rejectEmptyResponse {
                complete(transfer)
              }
            },
            delete {
              val userDeleted: Future[ActionPerformed] =
                (transferActor ? DeleteTransfer(id.toInt)).mapTo[ActionPerformed]
              onSuccess(userDeleted) { performed =>
                log.info("Deleted user [{}]: {}", id, performed.description)
                complete((StatusCodes.OK, performed))
              }
            }
          )
        },
        path(Segment / "status") { id =>
          put {
            entity(as[TransferStatus]) { status =>
              val transferUpdated: Future[ActionPerformed] =
                (transferActor ? UpdateTransferStatus(id.toInt, status)).mapTo[ActionPerformed]
              onSuccess(transferUpdated) { performed =>
                log.info("Updated transfer [{}]: {}", id, performed.description)
                complete((StatusCodes.OK, performed))
              }
            }
          }
        }
      )
    }

}
