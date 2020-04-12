object Main extends App {
  // Load new file
  val source = io.Source.fromInputStream(getClass.getResourceAsStream("coronavirus.txt"))
  val text = try source.mkString finally source.close()
  println(text)
}
