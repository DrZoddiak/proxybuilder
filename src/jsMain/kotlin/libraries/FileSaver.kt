package libraries

import org.w3c.files.Blob

@Suppress("FunctionName")
@JsModule("file-saver")
@JsNonModule
external fun FileSaver(data: Blob, filename: String = definedExternally)
