package com.mstringham.ipgeolocation.domain

import com.mstringham.ipgeolocation.ui.UiText

object Validation {

    private const val ZERO_TO_255 = "([0-9]|[1-9]\\d{1}|1\\d{2}|2[0-4]\\d|25[0-5])"
    private const val IP_V4_PATTERN = "$ZERO_TO_255\\.$ZERO_TO_255\\.$ZERO_TO_255\\.$ZERO_TO_255"
    private val ipv4Pattern = Regex(IP_V4_PATTERN)

    private const val DOMAIN_NAME_PATTERN = "^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}$"
    private val domainNamePattern = Regex(DOMAIN_NAME_PATTERN)

    fun sanitize(query: String) = query.trim()

    fun isQueryValid(query: String) =
        query.isEmpty() || ipv4Pattern.matches(query) || domainNamePattern.matches(query)

}

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: UiText? = null
)
