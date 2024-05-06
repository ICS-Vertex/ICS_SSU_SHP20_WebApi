package shp.ssu.icsvertex.nl.inventory

import nl.icsvertex.ktor.exposed.extensions.toResponse
import nl.icsvertex.ssu.shp.core.dao.InventoryOrderStatusEntity
import nl.icsvertex.ssu.shp.core.domain.InventoryOrderStatus
import nl.icsvertex.ssu.shp.core.dto.InventoryOrderStatusDto
import org.jetbrains.exposed.sql.transactions.transaction

fun getStatuses(): List<InventoryOrderStatusDto>{
    return transaction {
        InventoryOrderStatusEntity.find{
            InventoryOrderStatus.statusType eq "INVENTORYORDER"
        }.toResponse<InventoryOrderStatusEntity, InventoryOrderStatusDto>()
    }
}