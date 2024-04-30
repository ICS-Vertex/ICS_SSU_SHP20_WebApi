package shp.ssu.icsvertex.nl.inventory

import nl.icsvertex.ktor.exposed.extensions.toResponse
import nl.icsvertex.ssu.shp.core.dao.ArticleEntity
import nl.icsvertex.ssu.shp.core.domain.Article
import nl.icsvertex.ssu.shp.core.dto.ArticleDto
import org.jetbrains.exposed.sql.transactions.transaction

fun getArticles(customerNo: String) : List<ArticleDto>{
    var result : List<ArticleDto> = listOf()
    transaction {
      val articleEntities = ArticleEntity.find {
            Article.customerNo eq customerNo
        }

       result = articleEntities.toResponse<ArticleEntity ,ArticleDto>()

    }

    return result
}