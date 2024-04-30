package shp.ssu.icsvertex.nl.inventory

import nl.icsvertex.ssu.shp.core.dao.*
import nl.icsvertex.ssu.shp.core.domain.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import shp.ssu.icsvertex.nl.exceptions.InventoryOrderInsertException
import shp.ssu.icsvertex.nl.plugins.convertToLocalDateTimeViaInstant
import java.time.LocalDateTime

fun nl.icsvertex.ssu.shp.core.models.inventory.InventoryOrder.processInput(): String {
    val input = this
    return transaction {

        val inventoryOrderEntity = InventoryOrderEntity.find {
            InventoryOrder.orderNo eq input.orderNo and (InventoryOrder.customerNo eq input.customerNo)
        }.apply {
            if (this.empty()) InventoryOrderEntity.new {
                orderNo = input.orderNo
                customerNo = input.customerNo
                type = input.type
                location = input.location
                locationName = input.locationName
                requestedEndDateTime = input.requestedEndDateTime.convertToLocalDateTimeViaInstant()
                externalReference = input.externalReference
                creationDate = LocalDateTime.now()
                creationUser = "ProcessInput"
                creationProgram = "Shopscan_Web_Api"
                mutationDate = LocalDateTime.now()
                mutationUser = "ProcessInput"
                mutationProgram = "Shopscan_Web_Api"
            }
            else {
                forEach {
                    it.apply {
                        mutationDate = LocalDateTime.now()
                    }
                }
            }
        }.firstOrNull()

        if (inventoryOrderEntity == null) {
            throw InventoryOrderInsertException("Insert failed miserably")
        }

        val articleMap: LinkedHashMap<String, ArticleEntity> = linkedMapOf()

        input.articles.forEach { article ->
            val articleEntity = ArticleEntity.find {
                Article.customerNo eq input.customerNo and
                        (Article.itemNo eq article.itemNo) and
                        (Article.variantCode eq article.variantCode)
            }.apply {
                if (this.empty()) {
                    ArticleEntity.new {
                        customerNo = input.customerNo
                        itemNo = article.itemNo
                        variantCode = article.variantCode
                        description = article.description
                        description2 = article.description2
                        brand = article.brand
                        collection = article.collection
                        vertComponent = article.vertComponent
                        horzComponent = article.horzComponent
                        merchandiseCode = article.merchandiseCode
                        merchandiseDescription = article.merchandiseDescription
                        season = article.season
                        subSeason = article.subSeason
                        theme = article.theme
                        vendorItemNo = article.vendorItemNo
                        materialCode = article.materialCode
                        creationDate = LocalDateTime.now()
                        creationUser = "ProcessInput"
                        creationProgram = "Shopscan_Web_Api"
                        mutationDate = LocalDateTime.now()
                        mutationUser = "ProcessInput"
                        mutationProgram = "Shopscan_Web_Api"
                    }
                } else forEach {
                    var changed = false
                    if (it.description != article.description) {
                        changed = true
                    }
                    if (!changed && it.description2 != article.description2) {
                        changed = true
                    }
                    if (!changed && it.brand != article.brand) {
                        changed = true
                    }
                    if (!changed && it.collection != article.collection) {
                        changed = true
                    }
                    if (!changed && it.vertComponent != article.vertComponent) {
                        changed = true
                    }
                    if (!changed && it.horzComponent != article.horzComponent) {
                        changed = true
                    }
                    if (!changed && it.merchandiseCode != article.merchandiseCode) {
                        changed = true
                    }
                    if (!changed && it.merchandiseDescription != article.merchandiseDescription) {
                        changed = true
                    }
                    if (!changed && it.season != article.season) {
                        changed = true
                    }
                    if (!changed && it.subSeason != article.subSeason) {
                        changed = true
                    }
                    if (!changed && it.theme != article.theme) {
                        changed = true
                    }
                    if (!changed && it.vendorItemNo != article.vendorItemNo) {
                        changed = true
                    }
                    if (!changed && it.materialCode != article.materialCode) {
                        changed = true
                    }
                    if (changed) it.apply {
                        description = article.description
                        description2 = article.description2
                        brand = article.brand
                        collection = article.collection
                        vertComponent = article.vertComponent
                        horzComponent = article.horzComponent
                        merchandiseCode = article.merchandiseCode
                        merchandiseDescription = article.merchandiseDescription
                        season = article.season
                        subSeason = article.subSeason
                        theme = article.theme
                        vendorItemNo = article.vendorItemNo
                        materialCode = article.materialCode
                        mutationDate = LocalDateTime.now()
                        mutationUser = "ProcessInput"
                        mutationProgram = "Shopscan_Web_Api"
                    }
                }
            }.firstOrNull()

            if (articleEntity != null){
                articleMap.putIfAbsent(articleEntity.itemNo + "~" + articleEntity.variantCode, articleEntity)
                article.barcodes.forEach { loopBarcode ->
                    ArticleBarcodeEntity.find {
                        ArticleBarcode.articleId eq articleEntity.recordId  and
                                (ArticleBarcode.barcode eq loopBarcode.barcode) }
                        .apply {
                            if (this.empty()){
                                ArticleBarcodeEntity.new {
                                    articleId = articleEntity
                                    barcode = loopBarcode.barcode
                                    barcodeType = loopBarcode.barcodeType
                                    unitOfMeasure = loopBarcode.unitOfMeasure
                                    isUniqueBarcode = loopBarcode.isUniqueBarcode
                                    qtyPerUnitOfMeasure = loopBarcode.qtyPerUnitOfMeasure
                                    creationDate = LocalDateTime.now()
                                    creationUser = "ProcessInput"
                                    creationProgram = "Shopscan_Web_Api"
                                    mutationDate = LocalDateTime.now()
                                    mutationUser = "ProcessInput"
                                    mutationProgram = "Shopscan_Web_Api"
                                }
                            } else forEach {
                                var changed = false
                                if (it.barcodeType != loopBarcode.barcodeType){
                                    changed = true
                                }
                                if (!changed && it.unitOfMeasure != loopBarcode.unitOfMeasure){
                                    changed = true
                                }
                                if (!changed && it.isUniqueBarcode != loopBarcode.isUniqueBarcode){
                                    changed = true
                                }
                                if (!changed && it.qtyPerUnitOfMeasure != loopBarcode.qtyPerUnitOfMeasure){
                                    changed = true
                                }
                                if (changed) it.apply{
                                    barcodeType = loopBarcode.barcodeType
                                    unitOfMeasure = loopBarcode.unitOfMeasure
                                    isUniqueBarcode = loopBarcode.isUniqueBarcode
                                    qtyPerUnitOfMeasure = loopBarcode.qtyPerUnitOfMeasure
                                    mutationDate = LocalDateTime.now()
                                    mutationUser = "ProcessInput"
                                    mutationProgram = "Shopscan_Web_Api"
                                }
                            }
                        }
                }
            }
        }


        input.lines.forEach { line ->
            InventoryOrderLineEntity.find {
                InventoryOrderLine.orderId eq inventoryOrderEntity.recordId.value and
                        (InventoryOrderLine.lineNo eq line.lineNo)
            }.apply {
                val helpEntity = articleMap[line.itemNo + "~" + line.variantCode]
                    ?: throw InventoryOrderInsertException("Article in line unknown in database. Check input file")
                if (this.empty()) InventoryOrderLineEntity.new {
                    orderId = inventoryOrderEntity
                    articleId = helpEntity
                    lineNo = line.lineNo
                    itemNo = line.itemNo
                    variantCode = line.variantCode
                    quantity = line.quantity
                    quantityHandled = 0.0
                    whseDocumentNo = line.whseDocumentNo
                    whseDocumentLineNo = line.whseDocumentLineNo
                    whseDocumentType = line.whseDocumentType
                    creationDate = LocalDateTime.now()
                    creationUser = "ProcessInput"
                    creationProgram = "Shopscan_Web_Api"
                    mutationDate = LocalDateTime.now()
                    mutationUser = "ProcessInput"
                    mutationProgram = "Shopscan_Web_Api"
                } else forEach {
                    it.apply {
                        itemNo = line.itemNo
                        variantCode = line.variantCode
                        quantity = line.quantity
                        whseDocumentNo = line.whseDocumentNo
                        whseDocumentLineNo = line.whseDocumentLineNo
                        whseDocumentType = line.whseDocumentType
                        mutationDate = LocalDateTime.now()
                        mutationUser = "ProcessInput"
                        mutationProgram = "Shopscan_Web_Api"
                    }
                }
            }
        }

        input.settings.forEach { setting ->
            InventoryOrderSettingEntity.find {
                InventoryOrderSetting.orderId eq inventoryOrderEntity.recordId and
                        (InventoryOrderSetting.settingCode eq setting.settingCode)
            }.apply {
                if (this.empty()) {
                    InventoryOrderSettingEntity.new {
                        orderId = inventoryOrderEntity
                        settingCode = setting.settingCode
                        settingValue = setting.settingValue
                        creationDate = LocalDateTime.now()
                        creationUser = "ProcessInput"
                        creationProgram = "Shopscan_Web_Api"
                        mutationDate = LocalDateTime.now()
                        mutationUser = "ProcessInput"
                        mutationProgram = "Shopscan_Web_Api"
                    }
                } else forEach {
                    it.apply {
                        settingValue = setting.settingValue
                        mutationDate = LocalDateTime.now()
                        mutationUser = "ProcessInput"
                        mutationProgram = "Shopscan_Web_Api"
                    }
                }
            }
        }
        inventoryOrderEntity.orderNo
    }
}
