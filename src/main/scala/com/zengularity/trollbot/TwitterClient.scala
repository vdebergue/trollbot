package comp.zengularity.trollbot

import com.typesafe.config.{Config, ConfigFactory}

import twitter4j.{Twitter, TwitterFactory}
import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder

object TwitterClient {
  val conf: Config = ConfigFactory.load();
  val APP_KEY: String             = ""
  val APP_SECRET: String          = ""
  val ACCESS_TOKEN: String        = ""
  val ACCESS_TOKEN_SECRET: String = ""

  def apply(): Twitter = {
      val factory = new TwitterFactory(new ConfigurationBuilder().build())
      val t = factory.getInstance()
      t.setOAuthConsumer(APP_KEY, APP_SECRET)
      t.setOAuthAccessToken(new AccessToken(ACCESS_TOKEN, ACCESS_TOKEN_SECRET))
      t
  }
}
