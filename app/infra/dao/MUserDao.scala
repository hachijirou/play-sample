package infra.dao

import models.entity.User
import anorm.SQL
import anorm.SqlQuery
import play.api.db.DB
import play.api.Play.current
import scala.util.Try
import org.joda.time.DateTime

object MUserDao {

  def store(user: User): Boolean = DB.withConnection { implicit connection =>
    val created, updated = new DateTime()
    val addedRows = SQL("""insert
      into m_user
      values ({id}, {account}, {password_hash}, {email_address}, {status}, {created}, {updated})""").on(
        "id" -> user.id.value,
        "account" -> user.account,
        "password_hash" -> user.passwordHashing,
        "email_address" -> user.emailAddress,
        "status" -> user.status.code,
        "created" -> created,
        "updated" -> updated).executeUpdate()
    if (addedRows == 1) true else throw new RuntimeException("fail to update")
  }
}