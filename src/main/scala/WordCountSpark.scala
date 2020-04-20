import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object WordCountSpark {
  def main(args: Array[String]): Unit = {
    val files = "shakespeare/"
    val spark = SparkSession.builder.appName("Count and Save").master("local[2]").getOrCreate()
    val stopWords = spark.read.textFile(getClass.getResource("stopWords.txt").getPath).collect()
    val source = spark.read.textFile(getClass.getResource(files).getPath).cache()//text(getClass.getResource(files).getPath)//.cache()//.collect()
    val text = source.rdd.flatMap(line => line
      .replaceAll("\\W|\\d", " ")
      .trim
      .toLowerCase()
      .split(" ")
      .filter(!_.isEmpty)
      .filterNot(stopWords.contains))
//      .map(word => (word, 1)).reduceByKey(_+_).sortBy(-_._2).take(10)
    val count = text.map(word => (word, 1)).reduceByKey(_+_).sortBy(-_._2)
    count.coalesce(1, false).saveAsTextFile("output/count.txt")
    println(count.take(10).toList)
  }
}