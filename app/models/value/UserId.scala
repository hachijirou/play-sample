package models.value

import org.apache.commons.lang3.RandomStringUtils

case class UserId(val value: String)

object UserId {

  def apply: UserId = {
    new UserId(generateId)
  }

  /**
   * ユーザIDを生成する.
   *
   * @return 生成した16桁のユーザID
   */
  def generateId: String = {
    RandomStringUtils.randomAlphanumeric(16)
  }
}