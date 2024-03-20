package com.rockthejvm.reviewboard.services

import zio.*

import com.rockthejvm.reviewboard.http.requests.CreateCompanyRequest
import com.rockthejvm.reviewboard.domain.data.Company
import com.rockthejvm.reviewboard.repositories.CompanyRepository

// "business logic"
trait CompanyService {
  def create(req: CreateCompanyRequest): Task[Company]
  def getAll: Task[List[Company]]
  def getById(id: Long): Task[Option[Company]]
  def getBySlug(slug: String): Task[Option[Company]]
  def activate(id: Long): Task[Company]
}

class CompanyServiceLive private (repo: CompanyRepository) extends CompanyService {
  override def create(req: CreateCompanyRequest): Task[Company] =
    repo.create(req.toCompany)
  override def getAll: Task[List[Company]] =
    repo.getAll
  override def getById(id: Long): Task[Option[Company]] =
    repo.getById(id)
  override def getBySlug(slug: String): Task[Option[Company]] =
    repo.getBySlug(slug)
  def activate(id: Long): Task[Company] =
    repo.activate(id)
}

object CompanyServiceLive {
  val layer: ZLayer[CompanyRepository, Nothing, CompanyService] =
    ZLayer.fromFunction(repo => new CompanyServiceLive(repo))

  // same as
  // ZLayer {
  //   ZIO.service[CompanyRepository].map(repo => new CompanyServiceLive(repo))
  // }
}
