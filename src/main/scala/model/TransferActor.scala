package model

import akka.actor.{ Actor, ActorLogging, Props }

final case class Transfer(id: Int, accountFrom: String, accountTo: String, amount: Double, currency: String, status: String)
final case class Transfers(transfers: Seq[Transfer])

object TransferActor {
  final case class ActionPerformed(description: String)
  final case object GetTransfers
  final case class CreateTransfer(transfer: Transfer)
  final case class GetTransfer(id: Int)
  final case class DeleteTransfer(id: Int)

  def props: Props = Props[TransferActor]
}

class TransferActor extends Actor with ActorLogging {
  import TransferActor._

  var transfers: Set[Transfer] = Set(
    Transfer(1, "account1", "account2", 10.0, "EUR", "completed"),
    Transfer(2, "account3", "account4", 32.0, "EUR", "completed"),
    Transfer(3, "account2", "account4", 54.0, "EUR", "processing"),
    Transfer(4, "account3", "account1", 17.0, "EUR", "pending")
  )

  def receive: Receive = {
    case GetTransfers =>
      sender() ! Transfers(transfers.toSeq)
    case CreateTransfer(transfer) =>
      transfers += transfer
      sender() ! ActionPerformed(s"Transfer ${transfer.id} created.")
    case GetTransfer(id) =>
      sender() ! transfers.find(_.id == id)
    case DeleteTransfer(id) =>
      transfers.find(_.id == id) foreach { transfer => transfers -= transfer }
      sender() ! ActionPerformed(s"Transfer $id deleted.")
  }
}