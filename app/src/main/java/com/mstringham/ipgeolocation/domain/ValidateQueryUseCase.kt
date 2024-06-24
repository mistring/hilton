package com.mstringham.ipgeolocation.domain

import com.mstringham.ipgeolocation.R
import com.mstringham.ipgeolocation.domain.Validation.isQueryValid
import com.mstringham.ipgeolocation.ui.UiText
import javax.inject.Inject

class ValidateQueryUseCase @Inject constructor() {

    operator fun invoke(query: String): ValidationResult {
        if (!isQueryValid(query)) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(id = R.string.invalid_query)
            )
        }
        return ValidationResult(
            successful = true,
            errorMessage = null
        )
    }

}
