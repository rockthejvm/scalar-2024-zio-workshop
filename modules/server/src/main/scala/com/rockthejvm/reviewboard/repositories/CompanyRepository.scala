package com.rockthejvm.reviewboard.repositories

import zio.*
import io.getquill.*
import io.getquill.jdbczio.Quill

import com.rockthejvm.reviewboard.domain.data.Company

trait CompanyRepository {
  // CRUD
  def create(company: Company): Task[Company]
  def getAll: Task[List[Company]]
  def getById(id: Long): Task[Option[Company]]
  def getBySlug(slug: String): Task[Option[Company]]
  def update(id: Long, op: Company => Company): Task[Company]
  def delete(id: Long): Task[Company]
}

class CompanyRepositoryLive private (quill: Quill.Postgres[SnakeCase.type])
    extends CompanyRepository {

  import quill.* // some extension methods + macros

  // set up the table
  // schema
  inline given schema: SchemaMeta[Company] =
    schemaMeta[Company]("companies")
  // insert schema
  inline given insSchema: InsertMeta[Company] =
    insertMeta[Company](_.id) // id column omitted, generated automatically
  // update schema
  inline given upSchema: UpdateMeta[Company] =
    updateMeta[Company](_.id) // id column omitted, ignored when updating a row

  // INSERT INTO companies(....) VALUES (...)
  def create(company: Company): Task[Company] =
    run {
      query[Company]
        .insertValue(lift(company))
        .returning(c => c)
    }

  def getAll: Task[List[Company]] =
    run {
      query[Company]
    }

  def getById(id: Long): Task[Option[Company]] =
    run {
      query[Company]
        .filter(_.id == lift(id))
        .take(1) // limit 1
    }.map(_.headOption)

  def getBySlug(slug: String): Task[Option[Company]] =
    run {
      query[Company]
        .filter(_.slug == lift(slug))
        .take(1) // limit 1
    }.map(_.headOption)

  def update(id: Long, op: Company => Company): Task[Company] =
    for {
      current <- getById(id).someOrFail(
        new RuntimeException(s"Could not update company: id $id inexistent")
      )
      newCompany <- run {
        query[Company]
          .filter(_.id == lift(id))
          .updateValue(lift(op(current)))
          .returning(c => c)
      }
    } yield newCompany

  def delete(id: Long): Task[Company] =
    run {
      query[Company]
        .filter(_.id == lift(id))
        .delete
        .returning(c => c)
    }
}

object CompanyRepositoryLive {
  val layer =
    ZLayer {
      for {
        quill <- ZIO
          .service[Quill.Postgres[SnakeCase.type]] // the type of the SnakeCase OBJECT not the trait
      } yield new CompanyRepositoryLive(quill)
    }
}
