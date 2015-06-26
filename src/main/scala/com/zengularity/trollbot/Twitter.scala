package com.zengularity.trollbot

import com.typesafe.config.{Config, ConfigFactory}

import twitter4j._
import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder

case class Hashtag(val name: String) extends AnyVal

case class Tweet(
  author: String,
  content: String,
  parent: Option[Long] = None
) {
  def hashtags: Set[Hashtag] =
    content.split(" ").collect { case t if t.startsWith("#") => Hashtag(t) }.toSet
}

object TwitterApp {
  val conf: Config = ConfigFactory.load();
  val APP_KEY: String             = conf.getString("appKey")
  val ACCESS_TOKEN_SECRET: String = conf.getString("accessTokenSecret")
  val APP_SECRET: String          = conf.getString("appSecret")
  val ACCESS_TOKEN: String        = conf.getString("accessToken")

  lazy val client: Twitter = {

    val factory = new TwitterFactory(new ConfigurationBuilder().build())
    val t = factory.getInstance()
    t.setOAuthConsumer(TwitterApp.APP_KEY, TwitterApp.APP_SECRET)

    t.setOAuthAccessToken(new AccessToken(TwitterApp.ACCESS_TOKEN, TwitterApp.ACCESS_TOKEN_SECRET))

    t
  }

  lazy val streamConfig = new twitter4j.conf.ConfigurationBuilder()
    .setOAuthConsumerKey(APP_KEY)
    .setOAuthConsumerSecret(APP_SECRET)
    .setOAuthAccessToken(ACCESS_TOKEN)
    .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET)
    .build

  def statusListener(queue: collection.mutable.Queue[Tweet]) = new StatusListener() {
    def onStatus(status: Status) {
     println(status.getText)
     val t = Tweet(status.getUser().getName(), status.getText, Option(status.getInReplyToStatusId()))
     queue.enqueue(t)
    }
    def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {}
    def onTrackLimitationNotice(numberOfLimitedStatuses: Int) {}
    def onException(ex: Exception) { ex.printStackTrace }
    def onScrubGeo(arg0: Long, arg1: Long) {}
    def onStallWarning(warning: StallWarning) {}
  }

  def getStream(terms: Array[String] = Array[String]()): Iterator[Tweet] = {
    new Iterator[Tweet] {
      val queue = collection.mutable.Queue.empty[Tweet]
      val twitterStream = new TwitterStreamFactory(streamConfig).getInstance
      twitterStream.addListener(statusListener(queue))
      twitterStream.filter(new FilterQuery().track(terms))
      def hasNext: Boolean = !queue.isEmpty
      def next(): Tweet = queue.dequeue()
    }

  }
}
