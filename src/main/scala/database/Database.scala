package database

import reactivemongo.api._
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.BSONDocument
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Database {

  val collection = connect()

  def connect(): BSONCollection = {
    val driver = MongoDriver
    val connection = driver.connection(List("localhost"))

    val db=  connection("akka")
    db.collection("transfer")
  }

  def findAllTransfer():Future[List[BSONDocument]]={
    val query = BSONDocument()

    Database.collection
      .find(query)
      .cursor[BSONDocument]
      .collect[List]()
  }

}
