package components

import react.FC
import react.Props
import react.dom.html.ReactHTML.p

external interface CounterProps : Props {
    var cardCount : Int
}

val CardCounter = FC<CounterProps>  { props ->

    p {
        +"cards: ${props.cardCount}"
    }
}