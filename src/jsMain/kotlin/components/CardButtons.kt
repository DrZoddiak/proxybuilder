package components

import api.card.ArtCardAPI
import api.card.FinalizedCardAPI
import api.card.FinalizedCardAPI.getFinalCards
import api.library.DownloadAPI
import kotlinx.coroutines.launch
import mui.material.Button
import mui.material.ButtonColor
import mui.material.ButtonVariant
import org.w3c.dom.HTMLButtonElement
import react.FC
import react.Props
import react.StateSetter
import react.dom.events.MouseEventHandler
import scope

external interface CardButtonProps : Props {
    var searchSetter: StateSetter<List<String>>
}

val CardButtons = FC<CardButtonProps> { props ->
    val deleteHandler: MouseEventHandler<HTMLButtonElement> = {
        scope.launch {
            FinalizedCardAPI.removeCards()
            ArtCardAPI.deleteArtCards()
            props.searchSetter(emptyList())
        }
    }
    Button {
        +"Delete"
        variant = ButtonVariant.contained
        color = ButtonColor.error
        onClick = deleteHandler
    }
    Button {
        +"Download"
        variant = ButtonVariant.contained
        color = ButtonColor.primary
        onClick = {
            scope.launch {
                DownloadAPI.zipFiles(getFinalCards())
            }
        }
    }
    Button {
        +"Save"
        variant = ButtonVariant.contained
        color = ButtonColor.info
        onClick = {
            scope.launch {

            }
        }
    }
}