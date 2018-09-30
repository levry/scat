package scat.data

data class City (
    val id: Int,
    val name: String,
    val country: Entity,
    val region: Entity
)

data class CityData(
    val name: String,
    val country: Int,
    val region: Int
)