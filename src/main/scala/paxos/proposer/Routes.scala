package paxos.proposer

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.{Directives, Route}
import paxos.messages.{Accepted, Promise, Value}

import scala.concurrent.ExecutionContext

class Routes(proposerActor: ActorRef)(implicit val actorSystem: ActorSystem, val ec: ExecutionContext)
  extends Directives with SprayJsonSupport {

  def routes: Route =
    path("submit") {
      post {
        entity(as[Value]) { value =>
          println(s"Received $value")
          proposerActor ! value
          complete(OK)
        }
      }
    } ~
    path("promise") {
      post {
        entity(as[Promise]) { promise =>
          println(s"Received $promise")
          proposerActor ! promise
          complete(OK)
        }
      }
    } ~
      path("accepted") {
        post {
          entity(as[Accepted]) { accepted =>
            println(s"Received $accepted")
            proposerActor ! accepted
            complete(OK)
          }
        }
      }
}
