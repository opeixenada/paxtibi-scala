package paxos.messages

import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

case class Accepted(n: Int, v: Int)

object Accepted {
  implicit val fmt: RootJsonFormat[Accepted] = jsonFormat2(Accepted.apply)
}