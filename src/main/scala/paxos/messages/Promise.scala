package paxos.messages

import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

case class Promise(n: Int, acceptedProposal: Option[Accept] = None)

object Promise {
  implicit val fmt: RootJsonFormat[Promise] = jsonFormat2(Promise.apply)
}