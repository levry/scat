package scat.data

/**
 * @author levry
 */
data class City (
    val id: Int,
    val name: String,
    val country: Entity,
    val region: Entity
)