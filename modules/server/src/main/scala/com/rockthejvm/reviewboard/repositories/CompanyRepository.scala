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

class CompanyRepositoryLive private (quill: Quill.Postgres[SnakeCase]) extends CompanyRepository {

  def create(company: Company): Task[Company] =
    ZIO.fail(new RuntimeException("Fail... for now"))
  def getAll: Task[List[Company]] =
    ZIO.fail(new RuntimeException("Fail... for now"))
  def getById(id: Long): Task[Option[Company]] =
    ZIO.fail(new RuntimeException("Fail... for now"))
  def getBySlug(slug: String): Task[Option[Company]] =
    ZIO.fail(new RuntimeException("Fail... for now"))
  def update(id: Long, op: Company => Company): Task[Company] =
    ZIO.fail(new RuntimeException("Fail... for now"))
  def delete(id: Long): Task[Company] =
    ZIO.fail(new RuntimeException("Fail... for now"))
}

object CompanyRepositoryLive {
  val layer = ZLayer.fromFunction(new CompanyRepositoryLive(_))
}
