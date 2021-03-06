package paxos.acceptor

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest

import scala.concurrent.{ExecutionContext, Future}

object Main extends App {

  implicit val actorSystem: ActorSystem = ActorSystem()
  implicit val ec: ExecutionContext = actorSystem.dispatcher

  val requester = Http().singleRequest(_: HttpRequest)

  val proposerClient = new ProposerClient("http://0.0.0.0:9999", requester)

  val acceptorActor = actorSystem.actorOf(Props(new AcceptorActor(proposerClient)), "AcceptorActor")

  val routes = new Routes(acceptorActor)

  val host = "0.0.0.0"

  def exitOnFailure(futures: Future[_]*)(implicit ec: ExecutionContext): Unit = {
    for (f <- futures) f.failed.foreach { t =>
      println("Fatal error, exiting", t)
      System.exit(1)
    }
  }

  locally {
    val port = args.find(_.startsWith("port:")).map(_.substring("port:".length).toInt).getOrElse(9000)

    println(s"Starting Acceptor on port $port")

    val apiFuture = Http().bindAndHandle(routes.routes, host, port)

    sys.addShutdownHook {
      apiFuture
        .flatMap(_.unbind())
        .onComplete(_ => actorSystem.terminate())
    }

    exitOnFailure(apiFuture)
  }
}
