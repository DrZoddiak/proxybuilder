package components

import org.w3c.dom.HTMLFormElement
import org.w3c.dom.HTMLTextAreaElement
import react.FC
import react.Props
import react.dom.events.ChangeEventHandler
import react.dom.events.FormEventHandler
import react.dom.html.InputType
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.textarea
import react.useState

external interface InputProps : Props {
    var onSubmit: (String) -> Unit
}

val InputComponent = FC<InputProps> { props ->
    val (text, setText) = useState("")

    val submitHandler: FormEventHandler<HTMLFormElement> = {
        it.preventDefault()
        setText("")
        props.onSubmit(text)
    }

    val changeHandler: ChangeEventHandler<HTMLTextAreaElement> = {
        setText(it.target.value)
    }

    form {
        onSubmit = submitHandler
        textarea {
            onChange = changeHandler
            value = text
        }
        div {
            input {
                type = InputType.submit
                value = "Submit"
            }
        }
    }
}