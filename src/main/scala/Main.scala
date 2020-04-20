import scala.collection.mutable
import scala.util.{Failure, Success, Try}

object Main extends App {
  // Load new file
  val stopWords = loadFile("stopWords.txt").split("\n").toList
  val allWords = loadFile("coronavirus.txt")
  val wordsToTokens = removeNonAlpha(allWords)

  // testing
  val toto = loadFile("smallFile.txt")
  val cleanToto = removeNonAlpha(toto)
  val mapWordCounts = countWords(cleanToto, stopWords)
  var mapMostUsedWords = most10UsedWords(mapWordCounts)
  println(s"---> Result of Scala way -> \n ${mapMostUsedWords}")

  // Testing wordcount2
  val countWords = countWords2(cleanToto)
  println(s"---> Result for the Loop way -->\n $countWords")

  //  println(top10MostFrequentWord())
  def loadFile(filename: String): String = {
    getFileInformation(getClass.getResourceAsStream(filename)) {
      scala.io.Source.fromInputStream(_).mkString
    }
  }


  def getFileInformation[A <: AutoCloseable, B](resource: A)(block: A => B): B = {
    Try(block(resource)) match {
      case Success(result) =>
        resource.close()
        result
      case Failure(e) =>
        resource.close()
        throw e
    }
  }


  def removeNonAlpha(stringOfWords: String): List[String] = {
    stringOfWords.replaceAll("\\W|\\d", " ")
      .trim
      .toLowerCase()
      .split(" ")
      .toList
      .filter(!_.isEmpty)
  }

  def countWords(listOfWords: List[String], stopWords: List[String]): Map[String, Int] =
    listOfWords.filterNot(stopWords.contains).groupBy(identity).mapValues(_.size)

  def most10UsedWords(wordCount: Map[String, Int]): Map[String, Int] = wordCount.toSeq.sortBy(-_._2).take(2).toMap

  def countWords2(listOfWords: List[String]): Seq[(String, Int)] = {
    // Create empty Map
    var mapWordCounts = collection.mutable.Map[String, Int]()
    val listWithoutStopWords = listOfWords.filterNot(stopWords.contains)
    for (word <- listWithoutStopWords) {
      if (mapWordCounts contains word) {
        mapWordCounts(word) += 1
      } else {
        mapWordCounts += (word -> 1)
      }
    }
    mapWordCounts.toSeq.sortBy(-_._2).take(2)
  }
}