package api.library

import FinalizedCard
import io.ktor.client.fetch.*
import kotlinx.coroutines.await
import libraries.FileSaver
import libraries.JSZip
import libraries.generatorOptions
import libraries.invoke

internal object DownloadAPI {
    private const val fileName = "proxy.zip"
    private const val imageExtension = ".png"
    private const val imageFolder = "images"
    private const val generatorOption = "blob"

    suspend fun zipFiles(list: List<FinalizedCard>) {
        val zip = JSZip()
        val img = zip.folder(imageFolder)
        list.forEach { card ->
            card.imageUri?.let {
                fetch(it).then {
                    img?.file("${card.name}$imageExtension", it.blob())
                }.await()
            }
        }
        zip.generateAsync(generatorOptions(generatorOption)).then { blob ->
            FileSaver(blob, fileName)
        }.await()
    }
}