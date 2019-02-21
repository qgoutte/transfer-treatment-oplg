trait TransferService {

  def getTransfer(id: Int)

  def createTransfer(content: Transfer)

  def updateTransfer(id: Int, content: Transfer)

}
