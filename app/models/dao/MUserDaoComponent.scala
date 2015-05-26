package models.dao

import org.joda.time.DateTime
import anorm.NamedParameter.string
import anorm.SQL
import anorm.sqlToSimple
import models.entity.User
import play.api.Play.current
import play.api.db.DB
/**
 * MUserデータアクセスクラス
 *
 * Cakeパターン対応
 */
trait MUserDaoComponent {
  val mUserDao: MUserDao
  // IF
  trait MUserDao {
    def store(user: User): Boolean
  }
  // Impl for jdbc
  class JdbcMUserDao extends MUserDao {
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
}