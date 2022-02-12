package jp.numero.dagashiapp.ui

data class UiState<T>(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val data: T? = null,
    val error: Throwable? = null,
) {
    inline fun onState(
        loading: () -> Unit = {},
        loadSucceed: (data: T) -> Unit = {},
        loadFailed: (error: Throwable) -> Unit = {},
    ) {
        if (isLoading && !isRefreshing) {
            loading()
        } else if (data != null && error == null) {
            loadSucceed(data)
        } else if (error != null) {
            loadFailed(error)
        }
    }
}