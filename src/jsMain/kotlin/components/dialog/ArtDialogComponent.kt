package components.dialog

import ArtCard
import FinalizedCard
import csstype.Auto
import csstype.px
import mui.material.Dialog
import mui.material.DialogTitle
import mui.material.ImageList
import mui.system.sx
import react.*

external interface ArtDialogProps : Props {
    var modalIsOpen: Boolean
    var artList: List<ArtCard>
    var setIsOpen: StateSetter<Boolean>
    var setCard: StateSetter<FinalizedCard?>
}

internal val ArtDialogComponent = FC<ArtDialogProps> { props ->

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
            children = Fragment.create {
                props.artList.map { art ->
                    DialogChildren {
                        key = art.toString()
                        this.artCard = art
                        this.setCard = props.setCard
                        this.setDialogIsOpen = props.setIsOpen
                    }
                }
            }
        }
    }
}