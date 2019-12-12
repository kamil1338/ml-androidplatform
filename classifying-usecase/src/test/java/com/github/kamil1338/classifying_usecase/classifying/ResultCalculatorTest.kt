package com.github.kamil1338.classifying_usecase.classifying

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ResultCalculatorTest {

    val tested = ResultCalculator()

    @Test
    fun `given countedClasses when two models predicted the same class then result correct list`() {
        // given
        val x = TestMapEntry(1, 1f)
        val y = TestMapEntry(0, 0.99f)
        val z = TestMapEntry(0, 1f)

        // when
        val result = tested.countedClasses(x, y, z)

        // then
        assertTrue(result[0] == 2)
        assertTrue(result[1] == 1)
        assertTrue(result[2] == 0)
    }

    @Test
    fun `given countedClasses when each model predicted different class then result corrent list`() {
        // given
        val x = TestMapEntry(0, 0.99f)
        val y = TestMapEntry(1, 0.99f)
        val z = TestMapEntry(2, 0.99f)

        // when
        val result = tested.countedClasses(x, y, z)

        // then
        assertTrue(result[0] == 1)
        assertTrue(result[1] == 1)
        assertTrue(result[2] == 1)
    }

    @Test
    fun `given isUnclearCase when each model predicted different class then return true`() {
        // given
        val list = arrayListOf(1, 1, 1)

        // when
        val result = tested.isUnclearCase(list)

        // then
        assertTrue(result)
    }

    @Test
    fun `given isUnclearCase when at least two models predicted the same class then return false`() {
        // given
        val list = arrayListOf(2, 1, 0)

        // when
        val result = tested.isUnclearCase(list)

        // then
        assertFalse(result)
    }

    @Test
    fun `given getWinnerForComparator when used values then return the biggest probability index`() {
        // given
        val x = TestMapEntry(0, 0.7f)
        val y = TestMapEntry(1, 0.8f)
        val z = TestMapEntry(2, 0.6f)

        // when
        val result = tested.getWinnerForComparator(
            x,
            y,
            z,
            Comparator { e1, e2 -> e2.value.compareTo(e1.value) }
        )

        // then
        assertTrue(result == 1)
    }

    @Test
    fun `given getWinnerForComparator when used keys then return the most frequently appearing index`() {
        // given
        val x = TestMapEntry(0, 0.7f)
        val y = TestMapEntry(0, 0.8f)
        val z = TestMapEntry(1, 0.6f)

        // when
        val result = tested.getWinnerForComparator(
            x,
            y,
            z,
            Comparator { e1, e2 -> e1.key.compareTo(e2.key) }
        )

        // then
        assertTrue(result == 0)
    }

    @Test
    fun `given calculateWinner when there is NOT unclear case then returns correct label index`() {
        // given
        val x = TestMapEntry(0, 0.7f)
        val y = TestMapEntry(0, 0.8f)
        val z = TestMapEntry(1, 0.6f)

        // when
        val result = tested.calculateWinner(x, y, z)

        // then
        assertTrue(result == 0)
    }

    @Test
    fun `given calculateWinner when there is unclear case then returns correct label index`() {
        // given
        val x = TestMapEntry(0, 0.7f)
        val y = TestMapEntry(1, 0.8f)
        val z = TestMapEntry(2, 0.6f)

        // when
        val result = tested.calculateWinner(x, y, z)

        // then
        assertTrue(result == 1)
    }

    class TestMapEntry(override val key: Int, override val value: Float) : Map.Entry<Int, Float>
}