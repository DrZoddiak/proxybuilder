package components

import api.Api
import csstype.*
import emotion.react.css
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.dom.events.ChangeEventHandler
import react.dom.html.InputType
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.span
import react.useEffectOnce
import react.useState

external interface CheckboxProps : Props {
    var handleToggle: (Boolean) -> Unit
}

val SliderComponent = FC<CheckboxProps> { props ->
    val (checked, setChecked) = useState(true)
    useEffectOnce {
        setChecked(Api.uniqueSetting)
    }

    val changeHandler: ChangeEventHandler<HTMLInputElement> = {
        setChecked(!checked)
        props.handleToggle(checked)
    }

    div {

        input {
            this.checked = checked
            onChange = changeHandler
            className = ClassName("react-switch-checkbox")
            id = "react-switch-new"
            type = InputType.checkbox
        }
        div {
            css {
                display = Display.flex
                alignItems = AlignItems.center
                justifyContent = JustifyContent.spaceBetween
                cursor = Cursor.pointer
                width = 50.px
                height = 25.px
                borderRadius = 50.px
                position = Position.relative
                background = Color(if (checked) "gray" else "blue")
                transitionDelay = .2.s
            }
            label {
                className = ClassName("react-switch-label")
                htmlFor = "react-switch-new"
                span {
                    className = ClassName("react-switch-button")
                }
            }
        }
    }
}