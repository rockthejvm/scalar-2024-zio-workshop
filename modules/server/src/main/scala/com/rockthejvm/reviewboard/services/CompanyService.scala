package com.rockthejvm.reviewboard.services

import zio.*

import com.rockthejvm.reviewboard.http.requests.CreateCompanyRequest
import com.rockthejvm.reviewboard.domain.data.Company

// "business logic"
trait CompanyService {
  def create(req: CreateCompanyRequest): Task[Company]
  def getAll: Task[List[Company]]
  def getById(id: Long): Task[Option[Company]]
  def getBySlug(slug: String): Task[Option[Company]]
}

class CompanyServiceLive private extends CompanyService {
  override def create(req: CreateCompanyRequest): Task[Company] =
    ZIO.succeed(Company.dummy)
  override def getAll: Task[List[Company]] =
    ZIO.succeed(List(Company.dummy))
  override def getById(id: Long): Task[Option[Company]] =
    ZIO.succeed(Some(Company.dummy))
  override def getBySlug(slug: String): Task[Option[Company]] =
    ZIO.succeed(Some(Company.dummy))
}

object CompanyServiceLive {
  val layer = ZLayer.succeed(new CompanyServiceLive)
}
