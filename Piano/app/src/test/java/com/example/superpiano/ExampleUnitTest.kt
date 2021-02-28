package com.example.superpiano

import com.example.superpiano.util.millisecondsToSecondsFormat
import org.junit.Test

import org.junit.Assert.*

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun timeDisplayConversion_isCorrect() {
        assertEquals("00.001", millisecondsToSecondsFormat(1))
        assertEquals("00.010", millisecondsToSecondsFormat(10))
        assertEquals("00.100", millisecondsToSecondsFormat(100))
        assertEquals("01.234", millisecondsToSecondsFormat(1234))
    }
}