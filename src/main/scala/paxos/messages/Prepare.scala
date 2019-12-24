package paxos.messages

import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

case class Prepare(n: Int)

object Prepare {
  implicit val fmt: RootJsonFormat[Prepare] = jsonFormat1(Prepare.apply)
}