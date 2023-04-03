package components

import csstype.ClassName
import libraries.ProgressiveImage
import org.w3c.dom.HTMLImageElement
import react.*
import react.dom.events.MouseEventHandler
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.i
import react.dom.html.ReactHTML.img

external interface ImageProps : Props {
    var src: String
    var placeholder: String
    var alt : String
    var onClick : MouseEventHandler<HTMLImageElement>
    var onAuxClick : MouseEventHandler<HTMLImageElement>
    var totalCards : Int
}

val ImageComponent = FC<ImageProps> { props ->
    val (img, setImg) = useState("")
    val (placeholder, setPlaceholder) = useState("")
    useEffectOnce {
        setImg(props.src)
        setPlaceholder(props.placeholder)
    }
    ProgressiveImage {
        this.src = img
        this.placeholder = placeholder
        children = { image, _, _ ->
            div.create {
                className = ClassName("card")
                div {
                    className = ClassName(if (props.totalCards > 1) "caption" else "captionOff")
                    i {
                        className = ClassName("fa-solid fa-square-plus")
                    }
                }
                img {
                    className = ClassName("cardimg")
                    src = image
                    alt = props.alt
                    onClick = props.onClick
                    onAuxClick = props.onAuxClick
                    onContextMenu = {
                        it.preventDefault()
                    }
                }
            }
        }
    }
}

