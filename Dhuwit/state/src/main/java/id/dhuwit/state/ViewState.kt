package id.dhuwit.state

sealed class ViewState {
    abstract class Feature : ViewState()
    object Loading : ViewState()
    data class Error(val message: String?) : ViewState()
}
