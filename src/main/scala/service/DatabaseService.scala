package service

import actor.Transfer
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._
import util.Helpers._

object DatabaseService {

  //VARIABLES
  // To directly connect to the default server localhost on port 27017
  val mongoClient: MongoClient = MongoClient()

  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[Transfer]), DEFAULT_CODEC_REGISTRY)

  // WARNING DB needs "transfers-db" as name
  val database: MongoDatabase = mongoClient.getDatabase("transfers-db").withCodecRegistry(codecRegistry)

  // WARNING COLLECTION needs "transfers" as name
  val collection: MongoCollection[Transfer] = database.getCollection("transfers")

  //FUNCTIONS
  //Return all transfers
  def getAllTransfers: Seq[Transfer] = {
    collection.find().results()
  }

  //Return one transfer with this id
  def getTransferById(id: Integer): Transfer = {
    collection.find(equal("id", id)).headResult()
  }

  //Return all documents with the status in parameter
  def getTransfersByStatus(status: String): Seq[Transfer] = {
    collection.find(equal("status", status)).results()
  }

  //Add one transfer
  def addTransfer(doc: Transfer): Unit = {
    collection.insertOne(doc)
  }

  //Update one transfer
  def updateTransfer(id: Integer, status: String): Unit = {
    collection.updateOne(equal("id", id), set("status", status))
  }

  //Delete one transfer
  def deleteTransfer(id: Integer): Unit = {
    collection.deleteOne(equal("id", id))
  }

}
