@file:OptIn(ExperimentalMaterial3Api::class)

package com.mstringham.ipgeolocation.ui


import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mstringham.ipgeolocation.R


/**
 * App bar to display title and conditionally display the back navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeolocationTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}


sealed class UiText {
    /**
     * If we just want to display a dynamic string
     *
     * For example:
     * - a string that comes from an API error message, where we don't have a String Resource
     */
    data class DynamicString(
        val value: String
    ) : UiText()

    /**
     * @param id - the String Resource ID
     * @param args - any arguments, as needed for string parameters
     */
    data class StringResource(
        @StringRes val id: Int,
        val args: List<Any> = listOf()
    ) : UiText()

    /**
     * In the UI layer, use this function to unwrap the value of the String Resource ID
     */
    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(
                id,
                *args.toTypedArray()
            )
        }
    }

    /**
     * The corresponding Composable version of asString
     */
    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(
                id,
                *args.toTypedArray()
            )
        }
    }
}