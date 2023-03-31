package libraries

import csstype.ClassName
import org.w3c.dom.*
import react.*

external interface Styles {
    var content: CSSProperties?
    var overlay: CSSProperties?
}

external interface Aria {
    var labelledBy: String?
    var describedBy: String?
    var modal: Boolean?
}

external interface OnAfterOpenCallbackOptions {
    var overlayElement: Element
    var contentElement: HTMLDivElement
}

external interface OnAfterOpenCallback {
    var action: (OnAfterOpenCallbackOptions) -> Unit
}

external interface ModalProps : Props {
    var children: ReactNode?
    var isOpen: Boolean?
    var style: Styles?
    var portalClassName: String?
    var bodyOpenClassName: String?
    var htmlOpenClassName: String?
    var className: ClassName?
    var overlayClassName: ClassName?
    var appElement: dynamic
    var onAfterOpen: OnAfterOpenCallback?
    var onAfterClose: (() -> Unit)?
    var onRequestClose: (() -> Unit)?
    var closeTimeoutMS: Number?
    var ariaHideApp: Boolean?
    var shouldFocusAfterRender: Boolean?
    var shouldCloseOnOverlayClick: Boolean?
    var shouldCloseOnEsc: Boolean?
    var shouldReturnFocusAfterClose: Boolean?
    var preventScroll: Boolean?
    fun parentSelector(): HTMLElement
    var aria: Aria?
    var data: Any?
    var role: String?
    var contentLabel: String?
    var contentRef: ((HTMLDivElement) -> Unit)?
    var overlayRef: ((HTMLDivElement) -> Unit)?
    var overlayElement: ((PropsWithRef<HTMLDivElement>, ReactElement<Props>) -> ReactElement<Props>)?
    var contentElement: ((PropsWithRef<HTMLDivElement>, ReactNode) -> ReactElement<Props>)?
    var testId: String?
    var id: String?
}

external interface Portal {
    var overlay: HTMLDivElement?
    var content: HTMLDivElement?
}

external class ReactModal : FC<ModalProps> {
    var defaultStyles: Styles
    fun setAppElement(appElement: dynamic): Unit


    var portal: Portal?
}

@JsModule("react-modal")
@JsNonModule
external val Modal: ComponentClass<ModalProps>