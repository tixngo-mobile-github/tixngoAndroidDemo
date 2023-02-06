package io.tixngo.exampleAndroid.support

import java.util.*
import kotlin.random.Random

class RandomUtility {
    companion object {
        fun randomBoolean(): Boolean {
            return Random.nextBoolean()
        }
        fun randomFirstName() : String {
            val list = listOf("Henry", "William", "Geoffrey", "Jim", "Yvonne", "Jamie", "Leticia", "Priscilla", "Sidney", "Nancy", "Edmund", "Bill", "Megan")
            val index = Random.nextInt(0, list.size)
            return list[index]
        }
        fun randomLastName() : String {
            val list = listOf("Pearson", "Adams", "Cole", "Francis", "Andrews", "Casey", "Gross", "Lane", "Thomas", "Patrick", "Strickland", "Nicolas", "Freeman")
            val index = Random.nextInt(0, list.size)
            return list[index]
        }
        fun randomDateWithinDaysBeforeToday(numberOfDay: Long): Date {
            val max = Date().time - numberOfDay * 24 * 3600 * 1000
            val offset = Random.nextLong(max)
            return Date(offset)
        }

    }
}