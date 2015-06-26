package comp.zengularity.trollbot

import com.typesafe.config.{Config, ConfigFactory}

import twitter4j.{Twitter, TwitterFactory}
import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder

case class HashTag(name: String)

case class Tweet(
  author: String,
  content: String,
  parent: Option[Tweet] = None
) {
  def hashtags: Set[Hashtag] =
    content.split(" ").collect { case t if t.startsWith("#") => Hashtag(t) }.toSet
}

object TwitterApp {
  val APP_KEY: String             = conf.getString("appKey")
  val ACCESS_TOKEN_SECRET: String = conf.getString("accessTokenSecret")
  val APP_SECRET: String          = conf.getString("appSecret")
  val ACCESS_TOKEN: String        = conf.getString("accessToken")

  lazy val client: Twitter = {
    val conf: Config = ConfigFactory.load();

    new TwitterFactory(new ConfigurationBuilder().build())
        .getInstance()
        .setOAuthConsumer(TwitterApp.APP_KEY, TwitterApp.APP_SECRET)
        .setOAuthAccessToken(
          new AccessToken(TwitterApp.ACCESS_TOKEN, TwitterApp.ACCESS_TOKEN_SECRET))
  }
}
