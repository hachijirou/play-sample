package models.service

import infra.dao.MUserDaoComponent

trait UserServiceComponent {
  this: MUserDaoComponent =>
  val userService: UserService
  class UserService {
  }

}