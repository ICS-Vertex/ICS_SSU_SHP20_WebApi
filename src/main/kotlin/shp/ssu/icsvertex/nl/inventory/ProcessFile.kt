package shp.ssu.icsvertex.nl.inventory

import com.google.gson.Gson
import io.ktor.http.content.*
import io.ktor.server.plugins.*
import nl.icsvertex.ssu.shp.core.models.inventory.InventoryOrder
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


suspend fun processFiles(input: MultiPartData): List<String> = buildList {
    input.forEachPart { file ->
        when (file) {
            is PartData.FileItem -> {
                if (file.originalFileName!!.lowercase().contains(".zip")) {

                    val zipFile = ZipInputStream(file.streamProvider())

                    val inventoryOrder = buildList<InventoryOrder> {
                        var zipEntry: ZipEntry? = zipFile.nextEntry
                        while (zipEntry != null) {
                            if (!zipEntry.isDirectory) {
                                zipFile.bufferedReader(charset("UTF-8")).readText().let {
                                    add(Gson().fromJson(it, InventoryOrder::class.java))
                                }
                            }
                            zipEntry = zipFile.nextEntry
                        }
                    }
                    inventoryOrder.forEach { it.processInput().let { order ->
                        if (!contains(order))add(order)
                    } }
                }
            }

            else -> throw BadRequestException("Import failed")
        }
    }

}
