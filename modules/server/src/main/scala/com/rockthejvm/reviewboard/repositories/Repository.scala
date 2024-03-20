package com.rockthejvm.reviewboard.repositories

import io.getquill.jdbczio.Quill
import io.getquill.SnakeCase
import javax.sql.DataSource

import zio.*

object Repository {
  // Any -> DataSource
  val dataSourceLayer = Quill.DataSource.fromPrefix("rockthejvm.db")
  // DataSource -> Quill.Postgres[SnakeCase]
  val quillLayer = Quill.Postgres.fromNamingStrategy(SnakeCase)
  // Any -> Quill.Postgres[SnakeCase]
  val dataLayer = dataSourceLayer >>> quillLayer // "feeds into"

  // same as
  // val dataZIOEffect = for {
  //   dataSource <- ZIO.service[DataSource]
  //   quill      <- ZIO.service[Quill.Postgres[SnakeCase]]
  // } yield quill

  // val dataLayer = ZLayer {
  //   dataZIOEffect.provide(dataSourceLayer, quillLayer)
  // }
}
