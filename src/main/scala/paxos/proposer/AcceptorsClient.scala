package paxos.proposer

import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import paxos.messages.{Accept, Prepare}
import spray.json.RootJsonFormat

import scala.concurrent.{ExecutionContext, Future}

class AcceptorsClient(baseUrls: Seq[String], requester: HttpRequest => Future[HttpResponse])
                     (implicit val ec: ExecutionContext)
  extends RequestBuilding with SprayJsonSupport {

  def send(prepare: Prepare) = {
    println(s"> $prepare")
    broadcast(prepare, "prepare")
  }

  def send(accept: Accept) = {
    println(s"> $accept")
    broadcast(accept, "accept")
  }

  private def broadcast[T](body: T, path: String)(implicit fmt: RootJsonFormat[T]) = {
    Future.sequence {
      baseUrls.map { baseUrl =>
        requester(Post(s"$baseUrl/$path", body))
      }
    }
  }

}
