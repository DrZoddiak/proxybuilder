package components

import ArtCard
import FinalizedCard
import csstype.Auto
import csstype.px
import mui.material.Dialog
import mui.material.DialogTitle
import mui.material.ImageList
import mui.system.sx
import react.FC
import react.Props
import react.StateSetter
import react.create

external interface ArtDialogProps : Props {
    var modalIsOpen: Boolean
    var setIsOpen: StateSetter<Boolean>
    var artList: List<ArtCard>
    var setArtList: StateSetter<List<ArtCard>>
    var setDeckList: StateSetter<List<FinalizedCard>>
}

val ArtDialogComponent = FC<ArtDialogProps> { props ->



    Dialog {
        maxWidth = "md"
        open = props.modalIsOpen
        DialogTitle {
            +"Select an art card!"
        }
        onClose = { _, _ ->
            props.setIsOpen(false)
        }
        ImageList {
            sx {
                width = 546.px
                height = 803.px
                padding = 30.px
                margin = Auto.auto
                gap = 10.px
            }
            cols = 3
            rowHeight = 261
            children = DialogChildren.create {
                this.artList = props.artList
                this.setArtList = props.setArtList
                this.setDeckList = props.setDeckList
                this.setDialogIsOpen = props.setIsOpen
            }
        }
    }
}