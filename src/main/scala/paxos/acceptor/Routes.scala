package paxos.acceptor

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.{Directives, Route}
import paxos.messages.{Accept, Prepare}

import scala.concurrent.ExecutionContext

class Routes(acceptorActor: ActorRef)(implicit val actorSystem: ActorSystem, val ec: ExecutionContext)
  extends Directives with SprayJsonSupport {

  def routes: Route =
    path("prepare") {
      post {
        entity(as[Prepare]) { prepare =>
          println(s"Received $prepare")
          acceptorActor ! prepare
          complete(OK)
        }
      }
    } ~
      path("accept") {
        post {
          entity(as[Accept]) { accept =>
            println(s"Received $accept")
            acceptorActor ! accept
            complete(OK)
          }
        }
      }
}
