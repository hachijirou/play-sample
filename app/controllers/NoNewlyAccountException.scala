package controllers

/**
 * 既に使用済みのアカウントが存在した場合に投げられる例外
 */
case class NoNewlyAccountException(message: String = null, cause: Throwable = null)
  extends RuntimeException(message, cause)