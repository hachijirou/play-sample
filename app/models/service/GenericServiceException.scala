package models.service

/**
 * サービスで汎用的に利用する例外
 */
case class GenericServiceException(message: String = null, cause: Throwable = null)
  extends RuntimeException(message, cause)