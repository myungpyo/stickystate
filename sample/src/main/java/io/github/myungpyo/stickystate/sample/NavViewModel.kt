package io.github.myungpyo.stickystate.sample

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import io.github.myungpyo.stickystate.sample.support.MutableEventFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class NavViewModel(private val state: SavedStateHandle): ViewModel() {

    private val _navEvent: MutableSharedFlow<NavEvent> = MutableEventFlow()
    val navEvent = _navEvent.asSharedFlow()

    fun navTo(navEvent: NavEvent) {
        _navEvent.tryEmit(navEvent)
    }

    fun navBack() {
        _navEvent.tryEmit(NavEvent.Back)
    }

}

sealed interface NavEvent {
    object Primary: NavEvent
    data class Secondary(val userName: String): NavEvent
    object Back: NavEvent
}