package database

import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._

class TransferDatabase {

  //VARIABLES
  // To directly connect to the default server localhost on port 27017
  val mongoClient: MongoClient = MongoClient()

  // WARNING DB needs "transfers-db" as name
  val database: MongoDatabase = mongoClient.getDatabase("transfers-db")

  // WARNING COLLECTION needs "transfers" as name
  val collection: MongoCollection[Document] = database.getCollection("transfers");

  //FUNCTIONS
  //Return all transfers
  def getAllTransfers(): Unit ={
    return collection.find()
  }

  //Return one transfer with this id
  def getTransferById(id:Integer):Unit={
    return collection.find(equal("id",id))
  }

  //Return all documents with the status in parameter
  def getTransfersByStatus(status:String):Unit = {
    return collection.find(equal("status",status))
  }

  //Add one transfer
  def addTransfer(doc : Document): Unit ={
    collection.insertOne(doc)
  }

  //Update one transfer
  def updateTransfer(id:Integer,status:String ):Unit={
    collection.updateOne(equal("id", id), set("status", status))
  }

  //Delete one transfer
  def deleteTransfer(id:Integer):Unit={
    collection.deleteOne(equal("id", id))
  }
}
