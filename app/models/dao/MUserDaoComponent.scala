package models.dao

import org.joda.time.DateTime
import anorm.NamedParameter.string
import anorm.SQL
import anorm.sqlToSimple
import models.entity.User
import play.api.Play.current
import play.api.db.DB
import anorm.RowParser
import models.value.UserId
import models.value.code.USER_STATUS
/**
 * MUserデータアクセスクラス
 */
trait MUserDaoComponent {
  val mUserDao: MUserDao
  // IF
  trait MUserDao {
    /**
     * ユーザ情報を登録します.
     *
     * @param user ユーザエンティティ
     * @return ユーザエンティティ
     */
    def store(user: User): User
    /**
     * アカウントとパスワードをキーにユーザ情報を取得します.
     *
     * @param account アカウント
     * @param passwordHashing ハッシュ化されたパスワード
     * @param paser パーサーオブジェクト
     */
    def findByAccountAndPasswordHashing(
      account: String, passwordHashing: String)(implicit parser: RowParser[User]): Option[User]
  }
  // Impl for jdbc
  class JdbcMUserDao extends MUserDao {
    // ユーザ情報登録
    def store(user: User): User = DB.withConnection { implicit connection =>
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
      if (addedRows == 1) user else throw new GenericDaoException("fail to update")
    }
    // アカウントとパスワードをキーにユーザ情報を取得します.
    def findByAccountAndPasswordHashing(account: String, passwordHashing: String)
      (implicit parser: RowParser[User]): Option[User] = DB.withConnection { implicit connection =>
        val status = USER_STATUS.ACTIVE
        SQL("""select *
          from m_user
          where account = {account} and password_hash = {password_hash} and status = {status}""").on(
          "account" -> account,
          "password_hash" -> passwordHashing,
          "status" -> status.code).as(parser.singleOpt)
    }
  }
}