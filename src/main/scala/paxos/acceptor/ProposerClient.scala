package paxos.acceptor

import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import paxos.messages.{Accepted, Promise}

import scala.concurrent.{ExecutionContext, Future}

class ProposerClient(baseUrl: String, requester: HttpRequest => Future[HttpResponse])
                    (implicit val ec: ExecutionContext)
  extends RequestBuilding with SprayJsonSupport {

  def reply(promise: Promise) = {
    println(s"> $promise")
    requester(Post(s"$baseUrl/promise", promise))
  }

  def reply(accepted: Accepted) = {
    println(s"> $accepted")
    requester(Post(s"$baseUrl/accepted", accepted))
  }

}
