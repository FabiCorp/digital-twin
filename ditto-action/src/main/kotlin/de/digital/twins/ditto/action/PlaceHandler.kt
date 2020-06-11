package de.digital.twins.ditto.action

object PlaceHandler {

    private val placeMutableList: MutableList<Place> = mutableListOf()

    fun returnPlaceSize() : Int {
        return placeMutableList.size
    }

    fun addPlaceToList(id: Int) {
        placeMutableList.add(Place(id))
    }
}