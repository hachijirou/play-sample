package models.service

import models.dao.MUserDaoComponent

trait UserServiceComponent {
  this: MUserDaoComponent =>
  val userService: UserService
  class UserService {
  }

}