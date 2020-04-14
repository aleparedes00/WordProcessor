import scala.util.{Failure, Success, Try}

object Main extends App {
  // Load new file
  val stopWords = loadFile("stopWords.txt").split("\n").toList
  val allWords = loadFile("coronavirus.txt")
  val wordsToTokens = removeNonAlpha(allWords)

  // testing
  val toto = loadFile("smallFile.txt")
  val cleanToto = removeNonAlpha(toto)
  println(s"---> Result of filterNot -> \n ${top10MostFrequentWord(cleanToto, stopWords)}")

  //  println(top10MostFrequentWord())
  def loadFile(filename: String): String = {
    loan(getClass.getResourceAsStream(filename)) {
      io.Source.fromInputStream(_).mkString
    }
  }


  def loan[A <: AutoCloseable, B](resource: A)(block: A => B): B = {
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

  def top10MostFrequentWord(listOfWords: List[String], stopWords: List[String]): List[(String, Int)] = {
    listOfWords.filterNot(stopWords.contains).map(word => (word, 1))
  }
}