package models.entity

import org.apache.commons.lang3.RandomStringUtils

import anorm.RowParser
import models.value.UserId
import models.value.UserId
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
  val userParser: RowParser[User] = {
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
  def apply(account: String, passwordHashing: String, emailAddress: String): User = {
    val id = UserId(User.generateId)
    val status = USER_STATUS.ACTIVE
    new User(id, account, passwordHashing, emailAddress, status)
  }

  /**
   * ユーザIDを生成する.
   *
   * @return 生成した16桁のユーザID
   */
  private def generateId: String = {
    RandomStringUtils.randomAlphanumeric(16)
  }


}