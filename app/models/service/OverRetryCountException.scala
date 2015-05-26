package models.service

/**
 * リトライ回数がオーバーした場合に投げられる例外
 */
case class OverRetryCountException(message: String = null, cause: Throwable = null)
  extends RuntimeException(message, cause)