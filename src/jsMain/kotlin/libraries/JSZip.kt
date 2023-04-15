@file:Suppress("unused", "UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")

package libraries

import io.ktor.client.fetch.*
import io.ktor.utils.io.core.*
import org.khronos.webgl.*
import org.w3c.dom.*
import org.w3c.dom.events.*
import org.w3c.dom.parsing.*
import org.w3c.dom.svg.*
import org.w3c.dom.url.*
import org.w3c.fetch.*
import org.w3c.files.*
import org.w3c.files.Blob
import org.w3c.notifications.*
import org.w3c.performance.*
import org.w3c.workers.*
import org.w3c.xhr.*
import kotlin.js.*

@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
@JsModule("jszip")
@JsNonModule
sealed external interface JSZip {
    companion object

    fun file(path: String, data: Promise<Any>): JSZip /* this */
    fun folder(name: String): JSZip?
    fun generateAsync(
        options: JSZipGeneratorOptions = definedExternally,
        onUpdate: (metadata: JSZipMetadata) -> Unit = definedExternally
    ): Promise<Blob>

}

sealed external interface JSZipMetadata {
    var percent: Number
    var currentFile: String?
}

sealed external interface JSZipGeneratorOptions {
    var type: String?
        get() = definedExternally
        set(value) = definedExternally
}

inline operator fun JSZip.Companion.invoke(): JSZip = js("JSZip()") as JSZip

fun generatorOptions(type: String? = ""): JSZipGeneratorOptions {
    val o = js("({})")
    o["type"] = type
    return o as JSZipGeneratorOptions
}
