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
        loadSucceed: (data: T) -> Unit = {},
        initialFailed: (error: Throwable) -> Unit = {},
        loadFailed: (error: Throwable) -> Unit = {},
    ) {
        if (isInitialLoading) {
            initialLoading()
        } else if (data == null && error != null) {
            initialFailed(error)
        } else if (data != null && error == null) {
            loadSucceed(data)
        } else if (error != null) {
            loadFailed(error)
        }
    }
}