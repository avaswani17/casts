package com.org.cast

import com.org.cast.Constants._
import java.sql.{Connection, DriverManager}
import com.typesafe.config.ConfigFactory

object DBOperations{
  def getConnection(): Connection = {
    val config = ConfigFactory.load()
    val driver = config.getString("db.default.driver")
    val url = config.getString("db.default.url")
    Class.forName(driver)
    DriverManager.getConnection(url, "sa", "")
  }

  def runQuery(connection: Connection,query:String):Unit={
    val statement = connection.createStatement()
    statement.executeUpdate(query)
  }
}
