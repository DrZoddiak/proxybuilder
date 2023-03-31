package components

import csstype.ClassName
import libraries.Modal
import react.FC
import react.Props
import react.VFC

external interface ModalContent : Props {
    var content: VFC
    var onClose: () -> Unit
    var onRequestClose : () -> Unit
    var isOpen: Boolean
}

val modalComponent = FC<ModalContent> { props ->
    Modal {
        className = ClassName("Modal")
        overlayClassName = ClassName("Overlay")

        ariaHideApp = false
        preventScroll = false
        shouldCloseOnEsc = true
        shouldFocusAfterRender = true
        shouldCloseOnOverlayClick = true
        shouldReturnFocusAfterClose = true

        isOpen = props.isOpen
        onAfterClose = props.onClose
        onRequestClose = props.onRequestClose
        props.content()
    }
}