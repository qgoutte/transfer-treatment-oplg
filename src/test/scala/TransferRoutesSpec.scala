import actor.{Transfer, TransferActor, TransferStatus, Transfers}
import akka.actor.ActorRef
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{ContentTypes, MessageEntity, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.concurrent.ScalaFutures
import route.TransferRoutes

class TransferRoutesSpec extends WordSpec with Matchers with ScalaFutures with ScalatestRouteTest with TransferRoutes {

  override val transferActor: ActorRef = system.actorOf(TransferActor.props, "transferActor")

  lazy val routes: Route = transferRoutes

  "TransferRoutes" should {
    "be able to get all transfers (GET /transfers)" in {
      val request = Get("/transfers")

      request ~> routes ~> check {
        status should ===(StatusCodes.OK)
        contentType should ===(ContentTypes.`application/json`)
        entityAs[Transfers].transfers.size should ===(4)
      }
    }

    "be able to get a transfer (GET /transfers/{id})" in {
      val request = Get("/transfers/1")

      request ~> routes ~> check {
        status should ===(StatusCodes.OK)
        contentType should ===(ContentTypes.`application/json`)
        entityAs[String] should ===("""{"accountFrom":"account1","accountTo":"account2","amount":10.0,"currency":"EUR","id":1,"status":"completed"}""")
      }
    }

    "be able to add transfer (POST /transfers)" in {
      val transfer = Transfer(5, "account1", "account2", 100.0, "EUR", "pending")
      val userEntity = Marshal(transfer).to[MessageEntity].futureValue

      val request = Post("/transfers").withEntity(userEntity)

      request ~> routes ~> check {
        status should ===(StatusCodes.Created)
        contentType should ===(ContentTypes.`application/json`)
        entityAs[String] should ===("""{"description":"Transfer 5 created."}""")
      }
    }

    "be able to update transfer (PUT /transfers/{id}/status)" in {
      val transferStatus = TransferStatus("processing")
      val statusEntity = Marshal(transferStatus).to[MessageEntity].futureValue

      val request = Put("/transfers/1/status").withEntity(statusEntity)

      request ~> routes ~> check {
        status should ===(StatusCodes.OK)
        contentType should ===(ContentTypes.`application/json`)
        entityAs[String] should ===("""{"description":"Transfer 1 updated with status 'processing'."}""")
      }
    }

    "be able to remove transfers (DELETE /transfers/{id})" in {
      val request = Delete(uri = "/transfers/1")

      request ~> routes ~> check {
        status should ===(StatusCodes.OK)
        contentType should ===(ContentTypes.`application/json`)
        entityAs[String] should ===("""{"description":"Transfer 1 deleted."}""")
      }
    }
  }

}
