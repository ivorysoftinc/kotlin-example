package com.ivorysoftinc.kotlin.example.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivorysoftinc.kotlin.example.data.Card
import com.ivorysoftinc.kotlin.example.data.EMPTY
import com.ivorysoftinc.kotlin.example.repository.CardsRepository
import com.ivorysoftinc.kotlin.example.repository.NetworkRepository
import com.ivorysoftinc.kotlin.example.resources.strings.SOMETHING_WENT_WRONG
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * ViewModel for working with [NetworkRepository] and [CardsRepository].
 * Also saves state of UI on configuration changes.
 */
class MainViewModel(
    private val networkRepository: NetworkRepository,
    private val cardsRepository: CardsRepository
) : ViewModel() {

    val cardsFlow: StateFlow<List<Card>>
        get() = _cardsFlow.asStateFlow()
    val loadingFlow: StateFlow<Boolean>
        get() = _loadingFlow.asStateFlow()
    val errorFlow: StateFlow<String>
        get() = _errorFlow.asStateFlow()

    private val _cardsFlow = MutableStateFlow(listOf<Card>())
    private val _loadingFlow = MutableStateFlow(true)
    private val _errorFlow = MutableStateFlow(EMPTY)

    private val job = SupervisorJob()
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
        _errorFlow.value = SOMETHING_WENT_WRONG
        setLoading(false)
        getCachedCards()
    }

    /**
     * Custom [CoroutineContext] for handling exceptions with [CoroutineExceptionHandler] above.
     */
    private val coroutineContext: CoroutineContext =
        Dispatchers.IO + job + coroutineExceptionHandler

    init {
        // Fetch data when ViewModel created.
        getCards()
    }

    // Cancel coroutineContext jobs.
    override fun onCleared() {
        coroutineContext.cancelChildren()
        super.onCleared()
    }

    // Listen for Network availability.
    fun getInternetUpdatesFlow(context: Context): Flow<Boolean> {
        networkRepository.initInternetConnectionUpdates(context)

        return networkRepository.connectivityFlow.onEach { connected ->
            if (connected) getCards()
        }
    }

    // Get cards from API.
    fun getCards() {
        setLoading(true)

        viewModelScope.launch(coroutineContext) {
            cardsRepository.getCards().collect {
                _cardsFlow.value = it
                setLoading(false)
            }
        }
    }

    // Get cached cards.
    private fun getCachedCards() {
        setLoading(true)

        viewModelScope.launch(coroutineContext) {
            cardsRepository.getCachedCards().collect {
                _cardsFlow.value = it
                setLoading(false)
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            _loadingFlow.value = true
            _errorFlow.value = EMPTY
        } else {
            _loadingFlow.value = false
        }
    }
}
