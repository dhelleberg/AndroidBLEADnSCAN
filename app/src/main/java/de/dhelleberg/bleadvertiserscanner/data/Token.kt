package de.dhelleberg.bleadvertiserscanner.data

data class Token(
    val serviceData: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Token

        if (!serviceData.contentEquals(other.serviceData)) return false

        return true
    }

    override fun hashCode(): Int {
        return serviceData.contentHashCode()
    }

}