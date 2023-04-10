package components

import react.VFC
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.i
import react.dom.html.ReactHTML.p

val TitleComponent = VFC {
    h1 { +"MTG Proxy Builder" }
    p { i { +"Powered by Ktor & Scryfall!" } }
}