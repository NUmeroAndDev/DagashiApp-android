package jp.numero.dagashiapp.ui

data class UiState<T>(
    val isInitialLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isMoreLoading: Boolean = false,
    val data: T? = null,
    val error: Throwable? = null,
) {
    val isLoading: Boolean
        get() = isInitialLoading || isRefreshing || isMoreLoading

    val isEmpty: Boolean
        get() = data == null && error == null

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

    fun handleData(data: T?): UiState<T> = copy(
        isInitialLoading = false,
        isRefreshing = false,
        isMoreLoading = false,
        data = data,
        error = null
    )

    fun handleError(error: Throwable): UiState<T> = copy(
        isInitialLoading = false,
        isRefreshing = false,
        isMoreLoading = false,
        error = error
    )
}