package com.alekseyvalyakin.roleplaysystem.base.image

abstract class DefaultImageProvider(
        protected val identifier: String
) : ImageProvider {

    override fun getId(): String {
        return identifier
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other !is ImageProvider) {
            return false
        }

        return identifier == other.getId()
    }

    override fun hashCode(): Int {
        return identifier.hashCode()
    }
}