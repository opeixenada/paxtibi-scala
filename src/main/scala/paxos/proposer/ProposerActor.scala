package paxos.proposer

import akka.actor.Actor
import paxos.messages.{Accept, Prepare, Promise, Value}

class ProposerActor(acceptorsClient: AcceptorsClient, quorum: Int) extends Actor {

  private var proposals: Map[Int, Int] = Map.empty
  private var promises: Map[Int, Seq[Promise]] = Map.empty
  private var maxProposalNumber: Int = 0

  override def receive: Receive = {
    case Value(v) =>
      val proposalNumber = maxProposalNumber + 1
      proposals = proposals + (proposalNumber -> v)
      maxProposalNumber = proposalNumber
      val proposal = Prepare(proposalNumber)
      println(s"Proposing $proposal")
      acceptorsClient.send(proposal)

    case p@Promise(n, _) =>
      val proposalPromises = promises.getOrElse(n, Seq.empty) :+ p
      promises = promises.removed(n) + (n -> proposalPromises)
      if (proposalPromises.length >= quorum) {
        println(s"Got quorum for proposal #$n!")
        val acceptedProposals = proposalPromises.flatMap(_.acceptedProposal)
        val value = if (acceptedProposals.nonEmpty) acceptedProposals.maxBy(_.n).v else proposals(n)
        acceptorsClient.send(Accept(n, value))
      }
  }
}
