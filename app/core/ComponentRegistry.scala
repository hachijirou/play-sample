package core

import models.service.UserServiceComponent
import infra.dao.MUserDaoComponent

trait ComponentRegistry extends
  UserServiceComponent with
  MUserDaoComponent
{
  lazy val userService = new UserService
  lazy val mUserDao = new JdbcMUserDao
}