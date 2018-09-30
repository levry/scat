package scat.data

data class Region (
    val id: Int,
    val name: String,
    val country: Country
)

data class RegionData(
    val name: String,
    val country: Int
)