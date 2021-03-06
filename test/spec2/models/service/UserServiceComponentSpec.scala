package spec2.models.service

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import core.TestComponentRegistry
import anorm.RowParser
import models.entity.User
import play.api.test._
import play.api.test.Helpers._
import models.value.UserId
import models.service.UserServiceComponent
import models.dao.MUserDaoComponent
import core.TestComponentRegistry
import core.TestComponentRegistry
import scala.reflect.ClassTag
import play.Logger
import models.service.OverRetryCountException

/**
 * UserServiceComponentのテスト
 *
 * @author k_hanamoto
 */
class UserServiceComponentSpec extends Specification with TestComponentRegistry {

  override val userService = new UserService

  "UserServiceComponent#isNewlyAccount" should {
    // 未登録のアカウント・パスワードの場合にTrueが返ることを確認する
    "return true for newly account and password" in {
      running(FakeApplication()) {
        mUserDao.findByAccountAndPasswordHashing(anyString, anyString)(any[RowParser[User]]) returns None
        userService.isNewlyAccount("newAccount", "xxxxx") must beTrue
      }
    }

    // 登録済みのアカウント・パスワードの場合にFalseが返ることを確認する
    "return false for not newly account and password" in {
      running(FakeApplication()) {
        val result = User(new UserId(UserId.generateId), "oldAccount", "xxxxx", "foo@gmail.com");
        mUserDao.findByAccountAndPasswordHashing(anyString, anyString)(any[RowParser[User]]) returns Some(result)
        userService.isNewlyAccount("oldAccount", "xxxxx") must beFalse
      }
    }
  }

  "UserServiceComponent#createAccount" should {
    // 1回目で登録が成功した場合にUserエンティティが返ることを確認する
    "return user for success of first store trial" in {
      running(FakeApplication()) {
        val result = User(new UserId(UserId.generateId), "success", "xxxxx", "foo@gmail.com")
        mUserDao.store(any[User]) returns result
        userService.createAccount("success", "xxxxx", "foo@gmail.com") must haveClass(ClassTag(classOf[User]))
      }
    }

    // 3回目で登録が成功した場合にUserエンティティが返ることを確認する
    "return user for success of third store trial" in {
      running(FakeApplication()) {
        // 呼び出し回数に応じて結果が変化するモックを作成
        val result = User(new UserId(UserId.generateId), "success", "xxxxx", "foo@gmail.com")
        var cnt = 0
        mUserDao.store(any[User]) answers { user =>
          cnt match {
            // 1回目
            case 0 => {
              cnt += 1
              throw new RuntimeException("first error.")
            }
            // 2回目
            case 1 => {
              cnt += 1
              throw new RuntimeException("second error.")
            }
            // 3回目
            case 2 => result
          }
        }
        userService.createAccount("success", "xxxxx", "foo@gmail.com") must haveClass(ClassTag(classOf[User]))
      }
    }

    // 登録が3回以上失敗した場合にエラーが返ることを確認する
    "return OverRetryCountException for fail of more third trial" in {
      running(FakeApplication()) {
        mUserDao.store(any[User]) throws new RuntimeException("error")
        userService.createAccount("fail", "xxxxx", "foo@gmail.com") must throwA[OverRetryCountException]
      }
    }
  }

}