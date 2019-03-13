package service

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

object MessagingService {

  val props = new Properties()

  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

  def publish(str: String): Unit = {
    val producer = new KafkaProducer[String, String](props)
    val message = new ProducerRecord[String, String]("transfert", null, str)
    producer.send(message)
    println(s"MESSAGE : $str")
  }

}
