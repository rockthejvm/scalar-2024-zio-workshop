package com.rockthejvm.reviewboard.pages

import com.raquo.laminar.api.L.{*, given}
import zio.*
import org.scalajs.dom
import java.time.Instant

import com.rockthejvm.reviewboard.domain.data.*
import com.rockthejvm.reviewboard.common.*
import com.rockthejvm.reviewboard.components.*
import com.raquo.laminar.DomApi
import scala.scalajs.js.Date

object CompanyPage {
  enum Status {
    case LOADING
    case NOT_FOUND
    case OK(company: Company)
  }

  // reactive variables
  // renderer

  def apply(id: Long) =
    div(
      cls := "container-fluid the-rock",
      /*
        TODO call backend for reviews of this company and
        - if the backend hasn't returned yet, renderLoading
        - if the company doesn't exist or some other error, renderNotFound
        - if the backend returns successfully, render
       */
      render(Company.dummy)
    )

  def renderLoading = List(
    div(
      cls := "simple-titled-page",
      h1("Loading...")
    )
  )

  def renderNotFound = List(
    div(
      cls := "simple-titled-page",
      h1("Oops!"),
      h2("This company doesn't exist."),
      a(
        href := "/",
        "Maybe check the list of companies again?"
      )
    )
  )

  def render(company: Company) = List(
    div(
      cls := "row jvm-companies-details-top-card",
      div(
        cls := "col-md-12 p-0",
        div(
          cls := "jvm-companies-details-card-profile-img",
          CompanyComponents.renderCompanyPicture(company)
        ),
        div(
          cls := "jvm-companies-details-card-profile-title",
          h1(company.name),
          div(
            cls := "jvm-companies-details-card-profile-company-details-company-and-location",
            CompanyComponents.renderOverview(company)
          )
        ),
        div(
          cls := "jvm-companies-details-card-apply-now-btn",
          button(
            `type` := "button",
            cls    := "btn btn-warning",
            "Add a review"
            // TODO disable button if in the process of adding a review
            // TODO some actions when the button is clicked
          )
        )
      )
    ),
    div(
      cls := "container-fluid"
      // TODO maybe add an "add review" card
      // TODO all the reviews for this company that already exist - call renderReview for all reviews
    )
  )

  def renderReview(review: Review) =
    div(
      cls := "container",
      div(
        cls := "markdown-body overview-section",
        div(
          cls := "company-description",
          div(
            cls := "review-summary",
            renderReviewDetail("Would Recommend", review.wouldRecommend),
            renderReviewDetail("Management", review.management),
            renderReviewDetail("Culture", review.culture),
            renderReviewDetail("Salary", review.salary),
            renderReviewDetail("Benefits", review.benefits)
          ),
          injectMarkdown(review),
          div(cls := "review-posted", s"Posted ${Time.unix2hr(review.created.toEpochMilli())}")
        )
      )
    )

  def renderReviewDetail(detail: String, score: Int) =
    div(
      cls := "review-detail",
      span(cls := "review-detail-name", s"$detail: "),
      (1 to score).toList.map(_ =>
        svg.svg(
          svg.cls     := "review-rating",
          svg.viewBox := "0 0 32 32",
          svg.path(
            svg.d := "m15.1 1.58-4.13 8.88-9.86 1.27a1 1 0 0 0-.54 1.74l7.3 6.57-1.97 9.85a1 1 0 0 0 1.48 1.06l8.62-5 8.63 5a1 1 0 0 0 1.48-1.06l-1.97-9.85 7.3-6.57a1 1 0 0 0-.55-1.73l-9.86-1.28-4.12-8.88a1 1 0 0 0-1.82 0z"
          )
        )
      )
    )

  def injectMarkdown(review: Review) =
    div(
      cls := "review-content",
      DomApi
        .unsafeParseHtmlStringIntoNodeArray(Markdown.toHtml(review.review))
        .map {
          case t: dom.Text         => span(t.data)
          case e: dom.html.Element => foreignHtmlElement(e)
          case _                   => emptyNode
        }
    )
}
