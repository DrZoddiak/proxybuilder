package components.dialog

import ArtCard
import FinalizedCard
import api.card.FinalizedCardAPI
import api.card.FinalizedCardAPI.replaceCard
import csstype.ClassName
import kotlinx.coroutines.launch
import mui.material.ImageListItem
import react.FC
import react.Props
import react.StateSetter
import react.dom.html.ImgLoading
import react.dom.html.ReactHTML.img
import react.key
import scope

external interface DialogChildrenProps : Props {
    var artCard: ArtCard
    var setCard: StateSetter<FinalizedCard?>
    var setDialogIsOpen: StateSetter<Boolean>
}

internal val DialogChildren = FC<DialogChildrenProps> { props ->
    ImageListItem {
        key = props.artCard.imageUri
        img {
            className = ClassName("cardimg")
            src = "${props.artCard.imageUri}?&auto=format"
            srcSet = "${props.artCard.imageUri}?&auto=format&dpr=2 2x"
            alt = props.artCard.name
            loading = ImgLoading.lazy
            onClick = {
                scope.launch {
                    val oldCard = FinalizedCardAPI.getFinalCards().find { it.name == props.artCard.name }
                    oldCard?.let { old -> replaceCard(old, props.artCard) }
                    props.setDialogIsOpen(false)
                    props.setCard(props.artCard.asFinalizedCard())
                }
            }
        }
    }
}