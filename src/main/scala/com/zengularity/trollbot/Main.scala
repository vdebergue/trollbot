package com.zengularity.trollbot

import akka.stream._
import akka.stream.scaladsl._
import akka.actor._
import akka.http.scaladsl.model._
import akka.http.scaladsl._

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

}
