package components

import ArtCard
import FinalizedCard
import api.card.ArtCardAPI
import api.card.FinalizedCardAPI
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
    var deckList: List<FinalizedCard>
    var setDeckList: StateSetter<List<FinalizedCard>>
    var setArtList: StateSetter<List<ArtCard>>
}

val CardButtons = FC<CardButtonProps> { props ->
    val deleteHandler: MouseEventHandler<HTMLButtonElement> = {
        scope.launch {
            FinalizedCardAPI.deleteFinalizedCards()
            ArtCardAPI.deleteArtCards()
            props.setArtList(emptyList())
            props.setDeckList(emptyList())
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
                DownloadAPI.zipFiles(props.deckList)
            }
        }
    }
}