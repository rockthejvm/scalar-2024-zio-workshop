package com.rockthejvm.reviewboard.repositories

import zio.*
import zio.test.*

import javax.sql.DataSource
import org.testcontainers.containers.PostgreSQLContainer

import com.rockthejvm.reviewboard.domain.data.*
import org.postgresql.ds.PGSimpleDataSource

object CompanyRepositorySpec extends ZIOSpecDefault {

  override def spec =
    suite("Company Repository")(
      test("create company") {
        val program = for {
          repo    <- ZIO.service[CompanyRepository]
          company <- repo.create(Company.dummy)
        } yield company

        assertZIO(program)(Assertion.assertion("company should be created") { value =>
          value.name == Company.dummy.name && value.url == Company.dummy.url
        })
      }
    ).provide(
      Scope.default,
      CompanyRepositoryLive.layer,
      Repository.quillLayer,
      TestContainers.testContainerDataSource
    )
}

object TestContainers {
  def createContainer() = {
    val container: PostgreSQLContainer[Nothing] =
      PostgreSQLContainer("postgres").withInitScript("sql/companies.sql")
    container.start()
    container
  }

  def createDataSource(container: PostgreSQLContainer[Nothing]): DataSource = {
    val dataSource = new PGSimpleDataSource()
    dataSource.setUrl(container.getJdbcUrl())
    dataSource.setUser(container.getUsername())
    dataSource.setPassword(container.getPassword())
    dataSource
  }

  val testContainerDataSource = ZLayer {
    for {
      container <- ZIO.acquireRelease(ZIO.attempt(createContainer())) { container =>
        ZIO.attempt(container.stop()).ignoreLogged
      }
      dataSource <- ZIO.attempt(createDataSource(container))
    } yield dataSource
  }
}
