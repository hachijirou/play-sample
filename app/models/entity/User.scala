package models.entity

import anorm.RowParser
import anorm.SqlParser.int
import anorm.SqlParser.str
import models.value.UserId
import models.value.code.USER_STATUS

/**
 * ユーザを表すエンティティ.
 *
 * private
 *
 * @param account
 */
case class User private (id: UserId, account: String, passwordHashing: String, emailAddress: String, status: USER_STATUS)

object User {

  // データマッピング
  implicit val userParser: RowParser[User] = {
    import anorm._
    import anorm.SqlParser._
    str("id") ~
      str("account") ~
      str("password_hash") ~
      str("email_address") ~
      int("status") map {
        case id ~ account ~ password_hash ~ email_address ~ status =>
          User(UserId(id), account, password_hash, email_address, USER_STATUS(status))
      }
  }

  /**
   * ユーザを表すエンティティを取得する.
   *
   * これは新規でユーザを登録する際に使用してください.
   *
   * @param account アカウント
   * @param passwordHashing ハッシュ化したパスワード
   * @param emailAddress メールアドレス
   * @return ユーザを表すエンティティ
   */
  def apply(id: UserId, account: String, passwordHashing: String, emailAddress: String): User = {
    val status = USER_STATUS.ACTIVE
    new User(id, account, passwordHashing, emailAddress, status)
  }

}