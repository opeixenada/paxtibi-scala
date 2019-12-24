package paxos.messages

import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

case class Value(v: Int)

object Value {
  implicit val fmt: RootJsonFormat[Value] = jsonFormat1(Value.apply)
}