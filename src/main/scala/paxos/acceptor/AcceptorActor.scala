package paxos.acceptor

import akka.actor.Actor
import paxos.messages.{Accept, Accepted, Prepare, Promise}

class AcceptorActor(proposerClient: ProposerClient) extends Actor {

  private var maxProposalNumber: Option[Int] = None
  private var acceptedProposal: Option[Accept] = None

  override def receive: Receive = {
    case p@Prepare(n) if maxProposalNumber.forall(n > _) =>
      println(s"Promising to $p")
      maxProposalNumber = Some(n)
      proposerClient.reply(Promise(n, acceptedProposal))

    case p@Prepare(_) =>
      println(s"Ignoring $p")

    case a@Accept(n, v) if maxProposalNumber.exists(n >= _) =>
      println(s"Accepting $a")
      acceptedProposal = Some(a)
      proposerClient.reply(Accepted(n, v))

    case a@Accept(_,_) =>
      println(s"Ignoring $a")
  }
}
