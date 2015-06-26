package com.zengularity.trollbot

import akka.stream._
import akka.stream.io._
import akka.stream.scaladsl._
import akka.actor._
import akka.http.scaladsl.model._
import akka.http.scaladsl._
import akka.util.ByteString

import scala.concurrent.duration._
import scala.concurrent._


object Main {
  def main(args: Array[String]) {
    println("-- Hello trollers --")
    args.map { arg =>
      println("Keywords:" + arg)
    }

    implicit val system = ActorSystem("reactive-tweets")
    implicit val mat = ActorMaterializer()

    TwitterApp.getStream(args)

    // val tweets: Source[Tweet, Unit] = ???

    // val connectionFlow = Http().outgoingConnectionTls("stream.twitter.com")
    // val baseRequest = HttpRequest(method= HttpMethods.POST, uri = "/1.1/statuses/filter.json")
    // val signedRequest = signedRequest(baseRequest)
    // val response = Source.single(HttpRequest(method= HttpMethods.POST, uri = "/1.1/statuses/filter.json"))
    //   .via(connectionFlow)
    //   .runWith(Sink.head)
    //
    // val res = Await.result(response, 1 second)
    // println(res)
    //
    //system.shutdown()
  }

  // def signedRequest(req: HttpRequest): HttpRequest = {
  //
  // }

  def twitterActorSource(terms: Array[String])(implicit mat: Materializer): Source[Tweet, Unit] = {
    val (actorRef, publisher) = Source.actorRef[Tweet](1000, OverflowStrategy.dropHead).toMat(Sink.publisher)(Keep.both).run()

    val twitterListener = new twitter4j.StatusListener() {
      def onStatus(status: twitter4j.Status) {
       val t = Tweet(status.getUser().getName(), status.getText, Option(status.getInReplyToStatusId()))
       actorRef ! t
      }
      def onDeletionNotice(statusDeletionNotice: twitter4j.StatusDeletionNotice) {}
      def onTrackLimitationNotice(numberOfLimitedStatuses: Int) {}
      def onException(ex: Exception) { ex.printStackTrace }
      def onScrubGeo(arg0: Long, arg1: Long) {}
      def onStallWarning(warning: twitter4j.StallWarning) {}
    }

    TwitterApp.setupStream(twitterListener, terms)
    Source(publisher)
  }

  def twitterQueueSource(terms: Array[String]): Source[Tweet, Unit] = {
    Source(() => TwitterApp.getStream(terms))
  }

  val fileSink: Sink[ByteString, Future[Long]] = SynchronousFileSink(new java.io.File("tweets.csv"), append = true)

}
