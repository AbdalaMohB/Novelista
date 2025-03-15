package com.hatman.novelista.EditorUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object RoutineHandler : ViewModel() {
    public fun newCoroutine(task: suspend () -> Any?){
        viewModelScope.launch(Dispatchers.Default) {
            task()
        }
    }
    public fun newCoroutineIO(task: suspend () -> Any?){
        viewModelScope.launch(Dispatchers.IO) {
            task()
        }
    }
    public fun newCoroutineUnconfined(task: suspend () -> Any?){
        viewModelScope.launch(Dispatchers.Unconfined) {
            task()
        }
    }
    public fun newCoroutineMain(task:() -> Any?){
        viewModelScope.launch {
            task()
        }
    }
}