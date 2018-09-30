package scat.data

data class School (
        val id: Int,
        val name: String,
        val number: Int?,
        val type: Entity,
        val country: Entity,
        val region: Entity?,
        val city: Entity
)

data class SchoolData (
        val name: String,
        val number: Int?,
        val type: Int,
        val city: Int
)