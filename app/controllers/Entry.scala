package controllers

import scala.util.Random
import scala.util.Try

import infra.dao.MUserDao
import models.entity.User
import play.api.data.Form
import play.api.data.Forms.email
import play.api.data.Forms.mapping
import play.api.data.Forms.nonEmptyText
import play.api.data.Forms.text
import play.api.libs.Crypto
import play.api.mvc.Action
import play.api.mvc.Controller

object Entry extends Controller {

  /**
   * 入力フォーム用のデータ
   *
   * @param account アカウント
   * @param password パスワード
   * @param passwordConfirm パスワード(確認)
   * @param emailAddress メールアドレスs
   */
  case class EntryData(
    account: String,
    password: String,
    passwordConfirm: String,
    emailAddress: String) {
    /* ハッシュ化したパスワードを返却する */
    def passwordHashing = Crypto.sign(password)
  }

  /* 入力フォームのマッピング */
  val entryForm = Form(
    mapping(
      "account" -> nonEmptyText(minLength = 1, maxLength = 32),
      "password" -> nonEmptyText(minLength = 1, maxLength = 18),
      "passwordConfirm" -> text,
      "emailAddress" -> email)(EntryData.apply)(EntryData.unapply).verifying(
        "パスワードが一致していません。",
        entryData => entryData.password == entryData.passwordConfirm))

  /**
   * 確認フォーム用のデータ
   *
   * @param account アカウント
   * @param passwordHashing ハッシュ化されたパスワード
   * @param emailAddress メールアドレス
   * @param csrfToken CSRF対策用のトークン
   */
  case class EntryConfirmData(account: String, passwordHashing: String, emailAddress: String, csrfToken: String)

  /* 確認フォームのマッピング */
  val entryConfirmForm = Form(
    mapping(
      "account" -> nonEmptyText,
      "passwordHashing" -> nonEmptyText,
      "emailAddress" -> email,
      "csrfToken" -> nonEmptyText)(EntryConfirmData.apply)(EntryConfirmData.unapply))

  /**
   * アカウント登録TOPページ
   */
  def top = Action {
    Ok(views.html.Entry.top(entryForm))
  }

  /**
   * アカウント登録確認ページ
   */
  def confirm = Action { implicit request =>
    entryForm.bindFromRequest.fold(
      errors => BadRequest(views.html.Entry.top(errors)),
      entryData => {
        // フレームワークのCSRF対策は挙動が不明なので独自でワンタイムチケットの発行
        val csrfToken = Random.alphanumeric.take(12).mkString
        Ok(views.html.Entry.confirm(entryData, csrfToken)).withSession(
          request.session + ("csrfToken" -> csrfToken))
      })
  }

  /**
   * アカウント登録完了ページ
   */
  def complete = Action { implicit request =>
    entryConfirmForm.bindFromRequest.fold(
      errors => throw new RuntimeException("Bad Request"),
      entryData => {
        // CSRFチェック
        request.session.get("csrfToken").map[Unit]{ sessionToken =>
          if (entryData.csrfToken != sessionToken) throw new RuntimeException("Bad Request")
        }.getOrElse(throw new RuntimeException("Bad Request"))

        // アカウント登録
        // 登録成功するまで最大3回リトライ
        Iterator.continually[Try[Boolean]]{
          val user = User(entryData.account, entryData.passwordHashing, entryData.emailAddress)
          Try(MUserDao.store(user))
        }.take(3).find(_.isSuccess).flatMap(_.toOption).map{ ret =>
          if (ret == false) throw new RuntimeException("fail to create account.")
        }.getOrElse(throw new RuntimeException("retry error!!"))

        Ok(views.html.Entry.complete("Entry complete page.")).withSession(
          request.session - "csrfToken")
      })
  }
}