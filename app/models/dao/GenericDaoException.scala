package models.dao

/**
 * サービスで汎用的に利用する例外
 */
case class GenericDaoException(message: String = null, cause: Throwable = null)
  extends RuntimeException(message, cause)