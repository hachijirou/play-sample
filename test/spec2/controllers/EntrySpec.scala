package spec2.controllers

import org.specs2.mutable.Specification
import anorm.RowParser
import controllers.Entry
import core.TestComponentRegistry
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration._
import play.api.test.FakeRequest

/**
 * Entryのテスト
 *
 * @author k_hanamoto
 */
class EntrySpec extends Specification with Results with TestComponentRegistry {

  class TestController() extends Controller with Entry with TestComponentRegistry

  "Entry#top" should {
    // リクエストがあった場合にステータスコードに200が返ることを確認する
    "return HTTP STATUS 200 for request." in {
      val controller = new TestController()
      val result: Future[Result] = controller.top.apply(FakeRequest())
      val response = Await.result(result, Duration(1000, MILLISECONDS))
      response.header.status must_== 200
    }
  }

  "Entry#confirm" should {
    // 正常なパラメータが指定された場合にステータスコード200が返ることを確認する
    "return HTTP STATUS 200 for request." in {
      running(FakeApplication()) {
        val parameters = Map(
          "account" -> Seq("aaa"),
          "password" -> Seq("aaa"),
          "passwordConfirm" -> Seq("aaa"),
          "emailAddress" -> Seq("ccc@gmail.com"))
        val fakeRequest = FakeRequest("POST", "/entry/confirm", FakeHeaders(), AnyContentAsFormUrlEncoded(parameters))
        val controller = new TestController()
        controller.userService.isNewlyAccount(anyString, anyString) returns true
        val result: Future[Result] = controller.confirm.apply(fakeRequest)
        val response = Await.result(result, Duration(1000, MILLISECONDS))
        response.header.status must_== 200
      }
    }
  }
}