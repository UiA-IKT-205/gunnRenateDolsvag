package com.example.superpiano.util

fun millisecondsToSecondsFormat(ms:Long):String {
    val second = (ms / 1000)  % 60
    val milliseconds = ms
    var newMilliseconds:Long = 0

    if (second < 1) {
        newMilliseconds = milliseconds
    } else {
        newMilliseconds = 1000 - ms
    }

    if (ms > 1000) {
        newMilliseconds = (1000 - ms) * -1
    }

    return String.format("%02d.%03d", second, newMilliseconds)
}