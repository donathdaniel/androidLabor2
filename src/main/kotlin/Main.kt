import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.Comparable

fun main(args: Array<String>) {

    //1
    val provider = DictionaryProvider
    val createDictionary = provider.createDictionary(DictionaryType.TREE_SET)

    val dict: IDictionary = createDictionary
    println("Number of words: ${dict.size()}")
    var word: String?
    while(true){
        print("What to find? ")
        word = readLine()
        if( word.equals("quit")){
            break
        }
        println("Result: ${word?.let { dict.find(it) }}")
    }

    //2
    val name = "John Smith Armageddon"
    println("\nMonogram of $name is ${name.createMonoram()}")

    val stringlist : List<String> = listOf("apple", "pear", "strawberry", "melon")
    println(Pair(stringlist,"#").joinBySeparator())

    println("Longest string of the list is ${stringlist.longestString()}\n")

    //3
    val dateNow = DateNow(1900,13,12)
    println("${dateNow.year} is leap year: ${dateNow.checkLeapYear()}")
    println("$dateNow is valid date: ${dateNow.valid()}")

    val validDates: MutableList<DateNow> = mutableListOf()
    println("Invalid dates: ")
    while(validDates.size < 10){
        val year = (0..2020).random()
        val month = (0..100).random()
        val day = (0..100).random()
        val dateNow2 = DateNow(year,month,day)
        if (dateNow2.valid()) validDates.add(dateNow2)
//        else println(dateNow2)
    }

    println("\nValid dates: ")
    validDates.forEach {println(it)}

    validDates.sort()
    println("\nSorted valid dates: ")
    validDates.forEach {println(it)}

    validDates.reverse()
    println("\nDescending order of valid dates: ")
    validDates.forEach {println(it)}

    validDates.sortWith(ComparatorOne)
    println("\nCustom sort of valid dates(ascending order by months): ")
    validDates.forEach {println(it)}
}

data class DateNow(val year: Int = LocalDateTime.now().year, val month: Int = LocalDateTime.now().monthValue, val day : Int = LocalDateTime.now().dayOfMonth) : Comparable<DateNow>{
    override fun compareTo(other: DateNow): Int = when{
        year != other.year -> year - other.year
        month != other.month -> month - other.month
        else -> day - other.day
    }

}

class ComparatorOne{
    companion object : Comparator<DateNow> {
        override fun compare(o1: DateNow, o2: DateNow): Int = when {
            o1.month != o2.month -> o1.month - o2.month
            else -> o1.day - o2.day
        }
    }
}

fun DateNow.checkLeapYear() : Boolean {
    if (year % 4 == 0)
        if (year % 100 == 0) {
            return year % 400 == 0
        } else {
            return true
        }
    else
        return false
}

fun DateNow.valid() : Boolean{
    val dateString = listOf<String>(year.toString(), month.toString(), day.toString()).joinToString("/")
    val df = SimpleDateFormat("yyyy/MM/dd")
    df.isLenient = false
    return try {
        df.parse(dateString)
        true
    } catch (e: ParseException) {
        false
    }
}

fun String.createMonoram() : String = this.split(" ").map { it[0] }.joinToString("")

fun Pair<List<String>, String>.joinBySeparator() : String = this.first.joinToString(this.second)

fun List<String>.longestString() : String? = this.maxByOrNull { it.length }

interface IDictionary{
    companion object ListDictionary: IDictionary{
        private val dictionary : MutableList<String> =  mutableListOf("list","egg","chicken","piper")
        override fun add(word: String): Boolean {
            return dictionary.add(word)
        }

        override fun find(word: String): Boolean {
            return dictionary.any{it == word}
        }

        override fun size(): Int {
            return dictionary.size
        }
    }

    fun add(word: String) : Boolean
    fun find(word: String) : Boolean
    fun size() : Int
}

enum class DictionaryType {
    ARRAY_LIST, TREE_SET, HASH_SET
}

object TreeSetDictionary : IDictionary{

    private val dictionary : TreeSet<String> =  TreeSet(listOf("tree","egg","chicken","piper"))

    override fun add(word: String): Boolean {
        return dictionary.add(word)
    }

    override fun find(word: String): Boolean {
        return dictionary.any{it == word}
    }

    override fun size(): Int {
        return dictionary.size
    }
}

object HashSetDictionary : IDictionary{

    private val dictionary : HashSet<String> =  HashSet(listOf("hash","egg","chicken","piper"))

    override fun add(word: String): Boolean {
        return dictionary.add(word)
    }

    override fun find(word: String): Boolean {
        return dictionary.any{it == word}
    }

    override fun size(): Int {
        return dictionary.size
    }
}

object DictionaryProvider{
    fun createDictionary(type : DictionaryType) : IDictionary{
        return when(type){
            DictionaryType.ARRAY_LIST -> IDictionary.ListDictionary
            DictionaryType.HASH_SET -> HashSetDictionary
            DictionaryType.TREE_SET -> TreeSetDictionary
        }
    }
}