package components

import ArtCard
import FinalizedCard
import api.card.ArtCardAPI
import api.card.ArtCardAPI.deleteArtCards
import api.card.FinalizedCardAPI
import csstype.ClassName
import kotlinx.coroutines.launch
import mui.material.CircularProgress
import mui.material.CircularProgressColor
import mui.material.ImageListItem
import react.*
import react.dom.html.ImgLoading
import react.dom.html.ReactHTML.img
import scope

external interface DialogChildrenProps : Props {
    var artList: List<ArtCard>
    var setArtList: StateSetter<List<ArtCard>>
    var setDeckList: StateSetter<List<FinalizedCard>>
    var setDialogIsOpen: StateSetter<Boolean>
}


val DialogChildren = FC<DialogChildrenProps> { props ->
    var isLoading by useState(true)
    useEffectOnce {
        isLoading = false
    }
    //TODO : Loader not effective
    CircularProgress {
        color = CircularProgressColor.secondary
        className = ClassName(if (isLoading) "" else "sneaky")
    }
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
                        deleteArtCards()
                        props.setArtList(ArtCardAPI.getArtCardList())
                    }
                }
            }
        }
    }
}