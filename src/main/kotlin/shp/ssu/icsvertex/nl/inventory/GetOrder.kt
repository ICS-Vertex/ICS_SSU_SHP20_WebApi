package shp.ssu.icsvertex.nl.inventory

import nl.icsvertex.ktor.exposed.extensions.toResponse
import nl.icsvertex.ssu.shp.core.dao.InventoryOrderEntity
import nl.icsvertex.ssu.shp.core.dao.InventoryOrderLineEntity
import nl.icsvertex.ssu.shp.core.domain.InventoryOrder
import nl.icsvertex.ssu.shp.core.domain.InventoryOrderLine
import nl.icsvertex.ssu.shp.core.dto.InventoryCompleteOrderDto
import nl.icsvertex.ssu.shp.core.dto.InventoryOrderDto
import nl.icsvertex.ssu.shp.core.dto.InventoryOrderLineDto
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import shp.ssu.icsvertex.nl.exceptions.InventoryOrderNotFoundException

fun getOrder(orderNo: String, customerNo: String): InventoryCompleteOrderDto{
    return transaction {
        InventoryOrderEntity.find{
            InventoryOrder.orderNo eq orderNo and (InventoryOrder.customerNo eq customerNo)}.
        firstOrNull()?.
        toResponse<InventoryOrderEntity, InventoryCompleteOrderDto>()
            ?: throw InventoryOrderNotFoundException("Not found in Database")
    }

}

fun getOrders(input: String): List<InventoryOrderDto>{
    return transaction {
        InventoryOrderEntity.find{
            InventoryOrder.customerNo eq input}.toResponse<InventoryOrderEntity, InventoryOrderDto>()
    }
}
