package paxos.messages

import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

case class Accept(n: Int, v: Int)

object Accept {
  implicit val fmt: RootJsonFormat[Accept] = jsonFormat2(Accept.apply)
}
