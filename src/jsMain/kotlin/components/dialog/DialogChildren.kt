package components.dialog

import ArtCard
import FinalizedCard
import api.card.FinalizedCardAPI
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
    var artList: List<ArtCard>
    var setDeckList: StateSetter<List<FinalizedCard>>
    var setDialogIsOpen: StateSetter<Boolean>
}

val DialogChildren = FC<DialogChildrenProps> { props ->
    props.artList.map { card ->
        ImageListItem {
            key = card.imageUri
            img {
                className = ClassName("cardimg")
                src = "${card.imageUri}?&auto=format"
                srcSet = "${card.imageUri}?&auto=format&dpr=2 2x"
                alt = card.name
                loading = ImgLoading.lazy
                onClick = {
                    scope.launch {
                        val oldCard = FinalizedCardAPI.getFinalizedCardList().find { it.name == card.name }
                        oldCard?.let { old -> FinalizedCardAPI.replaceFinalizedCard(old, card) }
                        props.setDialogIsOpen(false)
                        props.setDeckList(FinalizedCardAPI.getFinalizedCardList())
                    }
                }
            }
        }
    }
}