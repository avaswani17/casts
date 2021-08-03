package com.org.cast.service


import scala.concurrent.ExecutionContext.Implicits.global
import org.apache.log4j.Logger

import scala.collection.mutable.ListBuffer
import scala.util.control.NonFatal
import com.org.cast.models.Cast
import com.org.cast.Constants._
import java.sql.Connection
import scala.concurrent.Future

import javax.inject.Inject

class ServiceCallFunctions @Inject()(connection: Connection) {
  val logger = Logger.getLogger(this.getClass.getName)

  def sendCast(cast: Cast): Future[String] = Future {
    logger.info(">>>--- sendCast Request Received ---<<<")
    try {
      val statement = connection.createStatement()
      val sql = s"insert into cast values(${cast.originatorUserId},${cast.bondId},'${cast.side}',${cast.price.getOrElse(0)},${cast.quantity.getOrElse(0)},'${cast.status}',${cast.targetUserIds})"
      statement.executeUpdate(sql)
      logger.info(">>>--- Record Inserted ---<<<")
    } catch {
      case NonFatal(e) => throw new Exception(e.getMessage)
    }
    "Success"
  }

  def getActiveCasts(id: Int): Future[List[Cast]] = Future {
    logger.info(">>>--- getActiveCasts Request Received ---<<<")
    try {
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(s"SELECT * from cast where targetUserIds = $id and status = '$ACTIVE'")
      val listBuffer = new ListBuffer[Cast]()
      while (resultSet.next()) listBuffer += new Cast(resultSet.getInt("originatorUserId"), resultSet.getInt("bondId"), resultSet.getString("side"), Some(resultSet.getDouble("price")), Some(resultSet.getInt("quantity")), resultSet.getString("status"), resultSet.getInt("targetUserIds"))
      listBuffer.toList
    } catch {
      case NonFatal(e) => throw new Exception(e.getMessage)
    }
  }

  def cancelCast(originatorUserId: Int, bondId: Int, side: String):Future[String] = Future{
    logger.info(">>>--- cancelCast Request Received ---<<<")
    try {
      val statement = connection.createStatement()
      val sql = s"update cast set status = '$CANCELLED' where originatorUserId = $originatorUserId AND bondId = $bondId AND side = '$side'"
      statement.executeUpdate(sql)
      logger.info(">>>--- Record Updated ---<<<")
    } catch {
      case NonFatal(e) => throw new Exception(e.getMessage)
    }
    "Success"
  }
}

//cast should be unique based on originatorUserId: Int, bondId: Int, side: String