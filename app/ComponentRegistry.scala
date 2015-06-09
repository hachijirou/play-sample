package core

import models.service.UserServiceComponent
import models.dao.MUserDaoComponent

/**
 * 依存性の定義
 *
 * サービス・DAOを追加した場合には他の例に習ってここに定義すること
 * 単体テストの際にコンポーネントの切り替えを容易にする狙いがある
 */
trait ComponentRegistry extends
  UserServiceComponent with
  MUserDaoComponent
{
  /* define services */
  val userService = new UserService

  /* define daos */
  val mUserDao = new JdbcMUserDao
}