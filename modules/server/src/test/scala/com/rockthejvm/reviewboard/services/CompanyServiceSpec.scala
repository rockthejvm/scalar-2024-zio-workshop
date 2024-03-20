package com.rockthejvm.reviewboard.services

import zio.*
import zio.test.*

import com.rockthejvm.reviewboard.http.requests.CreateCompanyRequest
import com.rockthejvm.reviewboard.repositories.CompanyRepository
import com.rockthejvm.reviewboard.domain.data.Company

object CompanyServiceSpec extends ZIOSpecDefault {

  val repoStub = new CompanyRepository {
    def create(company: Company): Task[Company]        = ZIO.succeed(company)
    def getAll: Task[List[Company]]                    = ZIO.succeed(List())
    def getById(id: Long): Task[Option[Company]]       = ZIO.none
    def getBySlug(slug: String): Task[Option[Company]] = ZIO.none
    def update(id: Long, op: Company => Company): Task[Company] =
      ZIO.fail(new RuntimeException("nothing here"))
    def delete(id: Long): Task[Company] = ZIO.fail(new RuntimeException("nothing here"))
  }

  override def spec: Spec[TestEnvironment & Scope, Any] =
    suite("Company service")(
      test("create company") {
        val program = for {
          service <- ZIO.service[CompanyService]
          company <- service.create(CreateCompanyRequest("Rock the JVM", "https://rockthejvm.com"))
        } yield company

        assertZIO(program)(Assertion.assertion("company should be created") { value =>
          value.name == "Rock the JVM" && value.url == "https://rockthejvm.com"
        })
      }
    ).provide(
      CompanyServiceLive.layer,
      ZLayer.succeed(repoStub)
    )

}
