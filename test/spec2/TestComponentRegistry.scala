package core

import models.service.UserServiceComponent
import models.dao.MUserDaoComponent
import org.specs2.mock.Mockito

/**
 * 依存性の定義
 *
 * サービス・DAOを追加した場合には他の例に習ってここに定義すること
 * 単体テストの際にコンポーネントの切り替えを容易にする狙いがある
 */
trait TestComponentRegistry extends
  UserServiceComponent with
  MUserDaoComponent with
  Mockito
{
  /* define services */
  lazy val userService = mock[UserService]

  /* define daos */
  lazy val mUserDao = mock[JdbcMUserDao]
}