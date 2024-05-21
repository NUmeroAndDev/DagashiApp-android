package jp.numero.dagashiapp.ui

import androidx.compose.runtime.Stable

@Stable
data class UiState<T>(
    private val _isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val data: T? = null,
    val error: Throwable? = null,
) {

    val isLoading: Boolean
        get() = _isLoading || isRefreshing

    val isInitialLoading: Boolean
        get() = _isLoading && data == null

    inline fun onState(
        initialLoading: () -> Unit = {},
        initialFailed: (error: Throwable) -> Unit = {},
        loaded: (data: T, error: Throwable?) -> Unit = { _, _ -> },
    ) {
        if (isInitialLoading) {
            initialLoading()
        } else if (data == null && error != null) {
            initialFailed(error)
        } else if (data != null) {
            loaded(data, error)
        }
    }

    fun startLoading(isRefreshing: Boolean = false): UiState<T> = copy(
        _isLoading = true,
        isRefreshing = isRefreshing
    )

    fun endLoading(
        data: T? = null,
        error: Throwable? = null
    ): UiState<T> = copy(
        _isLoading = false,
        isRefreshing = false,
        data = data ?: this.data,
        error = error
    )
}