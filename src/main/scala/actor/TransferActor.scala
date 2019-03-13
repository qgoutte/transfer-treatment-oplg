package actor

import akka.actor.{Actor, ActorLogging, Props}
import service.DatabaseService
import spray.json.enrichAny
import util.JsonSupport

final case class Transfer(id: Int, accountFrom: String, accountTo: String, amount: Double, currency: String, var status: String)
final case class Transfers(transfers: Seq[Transfer])
final case class TransferStatus(value: String)

final case class TransferUpdateEvent(transfer: Transfer, newStatus: TransferStatus)
final case class TransferDeleteEvent(transfer: Transfer)

object TransferActor {
  final case class ActionPerformed(description: String)
  final case object GetTransfers
  final case class CreateTransfer(transfer: Transfer)
  final case class GetTransfer(id: Int)
  final case class UpdateTransferStatus(id: Int, status: TransferStatus)
  final case class DeleteTransfer(id: Int)

  def props: Props = Props[TransferActor]
}

class TransferActor extends Actor with ActorLogging with JsonSupport {
  import TransferActor._
  import service.MessagingService

  def receive: Receive = {
    case GetTransfers =>
      sender() ! Transfers(DatabaseService.getAllTransfers())

    case CreateTransfer(transfer) =>
      MessagingService.publish(transfer.toString)
      sender() ! ActionPerformed(s"Transfer ${transfer.id} created.")

    case GetTransfer(id) =>
      sender() ! DatabaseService.getTransferById(id)

    case UpdateTransferStatus(id, status) =>
      val tranferUpdate = TransferUpdateEvent(DatabaseService.getTransferById(id), status)
      MessagingService.publish(tranferUpdate.toJson.prettyPrint)
      sender() ! ActionPerformed(s"Transfer $id updated with status '${status.value}'.")

    case DeleteTransfer(id) =>
      val transferDelete = TransferDeleteEvent(DatabaseService.getTransferById(id))
      MessagingService.publish(transferDelete.toJson.prettyPrint)
      sender() ! ActionPerformed(s"Transfer $id deleted.")
  }
}
