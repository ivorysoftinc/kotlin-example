package com.ivorysoftinc.kotlin.example.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.ivorysoftinc.kotlin.example.extensions.networkCallback
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Repository class for working with network connection availability.
 */
@OptIn(ObsoleteCoroutinesApi::class)
class NetworkRepository {

    val connectivityFlow: Flow<Boolean>
        get() {
            connectivityChannel.trySend(checkConnection())
            return connectivityChannel.openSubscription()
                .consumeAsFlow()
                .conflate()
                .distinctUntilChanged()
        }

    private var connectivityManager: ConnectivityManager? = null
    private val connectivityChannel = BroadcastChannel<Boolean>(Channel.CONFLATED)

    /**
     * Call this fun to init [connectivityManager] with [Context].
     */
    fun initInternetConnectionUpdates(context: Context) {
        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

        val networkRequest =
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()

        connectivityManager?.registerNetworkCallback(
            networkRequest,
            networkCallback(
                onAvailable = { connectivityChannel.trySend(true) },
                onLost = { connectivityChannel.trySend(false) }
            )
        )
    }

    private fun checkConnection(): Boolean {
        return connectivityManager?.let { cm ->
            cm.getNetworkCapabilities(cm.activeNetwork)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } ?: false
    }
}
