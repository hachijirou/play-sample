package models.value.code

/**
 * ユーザステータス
 */
object USER_STATUS {
  // 有効
  case object ACTIVE extends USER_STATUS(1)
  // 一時停止
  case object STOP extends USER_STATUS(2)
  // 退会
  case object RESIGN extends USER_STATUS(3)
  // 強制退会
  case object FORCE_RESIGN extends USER_STATUS(4)
  // コードの配列
  val values = Array(ACTIVE, STOP, RESIGN, FORCE_RESIGN)

  /**
   * ステータスコードから適切なcaseオブジェクトを返す.
   *
   * @param code ステータスコード
   * @return ステータスコードを表すcaseオブジェクト
   */
  def apply(code: Int): USER_STATUS = {
    values.find(_.code == code).getOrElse {
      throw new IllegalArgumentException("Bad code = [" + code + "]")
    }
  }
}

sealed abstract class USER_STATUS(val code: Int) {
  val name = toString
}