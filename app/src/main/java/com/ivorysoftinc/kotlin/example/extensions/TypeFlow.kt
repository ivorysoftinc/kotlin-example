package com.ivorysoftinc.kotlin.example.extensions

import kotlinx.coroutines.flow.flow

/**
 * [flow] extensions and utils file.
 */

inline fun <T> typeFlow(crossinline block: suspend () -> T) = flow { emit(block()) }
