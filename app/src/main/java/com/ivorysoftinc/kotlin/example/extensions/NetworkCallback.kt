package com.ivorysoftinc.kotlin.example.extensions

import android.net.ConnectivityManager
import android.net.Network

/**
 * Network extensions and Utils file.
 */
inline fun networkCallback(crossinline onAvailable: () -> Unit, crossinline onLost: () -> Unit) =
    object : ConnectivityManager.NetworkCallback() {

        override fun onLost(network: Network) = onLost()

        override fun onAvailable(network: Network) = onAvailable()
    }
