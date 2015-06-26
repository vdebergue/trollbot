package com.zengularity.trollbot

import akka.stream._
import akka.stream.scaladsl._
import akka.actor._

object Main {
  def main(args: Array[String]) {
    println("-- Hello trollers --")

    implicit val system = ActorSystem("reactive-tweets")
    implicit val mat = ActorMaterializer()

    val tweets: Source[Tweet, Unit] = ???
  }

}

case class Tweet(content: String)
