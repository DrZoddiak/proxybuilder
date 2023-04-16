@file:Suppress("unused")
@file:JsModule("react-progressive-graceful-image")
@file:JsNonModule

package libraries

import org.w3c.dom.events.Event
import react.*

sealed external interface SrcSetData {
    var srcSet: String?
    var sizes: String?
}

external fun srcSetData(
    srcSet: String,
    sizes: String
): SrcSetData

sealed external interface ProgressiveImageProps : Props {
    var delay: Number?
    var onError: (Event) -> Unit //errorEvent : Event
    var placeholder: dynamic //String | Node
    var src: String
    var srcSetData: SrcSetData?
    var noRetry: Boolean?
    var noLazyLoad: Boolean?
    var rootMargin: String?
    var threshold: dynamic //Number | Array<Number>
    var children: ((src: String, loading: Boolean, srcSetData: SrcSetData?) -> ReactNode)?
}

@JsName("default")
external val ProgressiveImage: ComponentClass<ProgressiveImageProps>