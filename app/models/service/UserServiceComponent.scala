package models.service

import scala.util.Try

import core.ComponentRegistry
import models.dao.MUserDaoComponent
import models.entity.User
import models.value.UserId

trait UserServiceComponent {
  this: MUserDaoComponent =>
  val userService: UserService
  class UserService extends ComponentRegistry{

    /**
     * アカウントが未使用かどうかチェックする.
     *
     * @param account アカウント
     * @param passwordHashing ハッシュ化したパスワード
     * @return アカウントが未使用の場合はTrue、使用済みの場合はFalse
     */
    def isNewlyAccount(account: String, passwordHashing: String): Boolean = {
      Try(mUserDao.findByAccountAndPasswordHashing(account, passwordHashing)).recover({
        case e: Throwable => throw e
      }).get match {
        case None => true
        case _ => false
      }
    }

    /**
     * アカウントを登録する.
     *
     * @param account アカウント
     * @param passwordHashing ハッシュ化したパスワード
     * @param emailAddress Emailアドレス
     * @return ユーザエンティティ
     */
    def createAccount(account: String, passwordHashing: String, emailAddress: String): User = {
      // 登録成功するまで最大3回リトライ
      Iterator.continually[Try[User]] {
        val user = User(new UserId(UserId.generateId), account, passwordHashing, emailAddress)
        Try(mUserDao.store(user))
      }.take(3).find(_.isSuccess).flatMap(_.toOption).getOrElse{
        throw new OverRetryCountException("fail to create account. account = [" + account + "]")
      }
    }
  }

}